from fastapi import FastAPI
from pydantic import BaseModel
import requests
import statistics
import math

app = FastAPI()

# Ollama 的本地 API 地址 (确保你的 ollama 已经启动并在运行 qwen 模型)
OLLAMA_API_URL = "http://localhost:11434/api/generate"


# --- 1. 个人学情诊断的数据模型 ---
class RadarData(BaseModel):
    indicators: list
    scores: list


# --- 2. 班级走势预测的数据模型 ---
class ClassScoreData(BaseModel):
    course_name: str
    class_name: str
    scores: list[float]
    knowledge_points: list[str]


# --- 3. 学业风险预测的数据模型 (新增) ---
class RiskPredictData(BaseModel):
    homework_avg: float
    attendance_rate: float
    midterm_score: float


# ================= 接口 1：处理单人雷达图弱项诊断 =================
@app.post("/api/diagnose")
def generate_diagnosis(data: RadarData):
    try:
        indicators = data.indicators
        scores = data.scores

        if not indicators or not scores:
            return {"code": 400, "msg": "数据为空"}

        min_score = min(scores)
        min_index = scores.index(min_score)
        weakest_point = indicators[min_index]['name']

        max_score = max(scores)
        max_index = scores.index(max_score)
        strongest_point = indicators[max_index]['name']

        prompt = (
            f"你是一位资深的大学教授。现在有一位学生的考试成绩单如下：\n"
        )
        for i in range(len(indicators)):
            prompt += f"- {indicators[i]['name']}: 得分率 {scores[i]}%\n"

        prompt += (
            f"\n已知该生表现最好的是【{strongest_point}】(得分率{max_score}%)，"
            f"极其薄弱、急需补救的是【{weakest_point}】(得分率{min_score}%)。\n"
            f"请基于以上数据，用温和鼓励的语气，写一段约150字的个性化学情诊断与复习建议。"
            f"要求：直接输出正文，不要有任何开场白或多余的寒暄。"
        )

        payload = {
            "model": "qwen:1.8b",
            "prompt": prompt,
            "stream": False
        }

        response = requests.post(OLLAMA_API_URL, json=payload)
        response.raise_for_status()

        result = response.json()
        ai_reply = result.get("response", "AI未能生成有效报告。")

        return {"code": 200, "msg": "success", "data": ai_reply}

    except Exception as e:
        print(f"个人诊断接口报错: {e}")
        return {"code": 500, "msg": "error", "data": "【本地 AI 引擎异常】无法生成报告。"}


# ================= 接口 2：处理班级群体成绩走势预测 =================
@app.post("/api/class_diagnose")
def generate_class_diagnosis(data: ClassScoreData):
    try:
        scores = data.scores
        if not scores:
            return {"code": 400, "msg": "该班级暂无成绩数据"}

        total_students = len(scores)
        avg_score = round(statistics.mean(scores), 2)
        max_score = max(scores)
        min_score = min(scores)

        excellent = len([s for s in scores if s >= 90])
        good = len([s for s in scores if 75 <= s < 90])
        pass_count = len([s for s in scores if 60 <= s < 75])
        fail = len([s for s in scores if s < 60])
        pass_rate = round((total_students - fail) / total_students * 100, 2)

        # 把数组拼成一句话
        points_str = "、".join(data.knowledge_points) if data.knowledge_points else "该课程核心理论与实践"

        prompt = (
            f"你是一位行事干练、一针见血的大学教研主任。请对该班级的成绩进行【精简】的走势预测。\n\n"
            f"【数据盘】\n"
            f"平均分：{avg_score}分，及格率：{pass_rate}%\n"
            f"分布：优秀{excellent}人，良好{good}人，及格边界{pass_count}人，不及格{fail}人。\n"
            f"该课程涉及的核心知识点有：【{points_str}】\n\n"
            f"【你的任务】\n"
            f"请用 120字到150字 写一段极度精炼的分析报告。要求：\n"
            f"1. 一句话总结当前的成绩分布现状（不要重复报数据，直接说结论，如是否两极分化、是否大面积薄弱）。\n"
            f"2. 必须从上述提供的【核心知识点】中，挑选1到2个难度较大的知识点，预测学生在后续学习这些知识点时可能遭遇的瓶颈。\n"
            f"3. 给出1条极具针对性的干预建议。\n"
            f"要求：直接输出正文，严禁说废话、寒暄，必须显得极其专业和冷峻。"
        )

        payload = {
            "model": "qwen:1.8b",
            "prompt": prompt,
            "stream": False
        }
        response = requests.post(OLLAMA_API_URL, json=payload)
        response.raise_for_status()

        result = response.json()
        return {"code": 200, "msg": "success", "data": result.get("response", "生成失败")}

    except Exception as e:
        print(f"班级预测接口报错: {e}")
        return {"code": 500, "msg": "error", "data": "【本地 AI 引擎异常】无法生成班级报告。"}


# ================= 接口 3：学业风险逻辑回归预测 (新增功能) =================
@app.post("/api/predict_risk")
def predict_academic_risk(data: RiskPredictData):
    """
    接收学生的平时表现数据，使用 Sigmoid 函数评估期末不及格的风险概率。
    """
    try:
        hw_avg = data.homework_avg
        att_rate = data.attendance_rate
        mid_score = data.midterm_score

        # 1. 基础加权得分计算
        # 权重设计：平时分30%，出勤率折算百分制后占20%，期中考试占50%
        # 即使平时满分，如果期中崩了，基础分也会被拉下来
        base_score = (hw_avg * 0.3) + (att_rate * 100 * 0.2) + (mid_score * 0.5)

        # 2. Sigmoid 函数转换
        # 公式解析：1 / (1 + e^((x - 55) * 0.2))
        # 阈值设在 55 分左右，说明基础分低于 55 分时，挂科概率会急剧上升
        def calculate_sigmoid_risk(x):
            # 防止溢出
            try:
                exponent = (x - 55) * 0.2
                # 如果指数太大或太小，进行截断处理
                if exponent > 100: return 0.0
                if exponent < -100: return 1.0
                return 1.0 / (1.0 + math.exp(exponent))
            except OverflowError:
                return 1.0 if x < 55 else 0.0

        fail_probability = calculate_sigmoid_risk(base_score)

        # 3. 风险定级
        risk_level = "低风险"
        if fail_probability > 0.7:
            risk_level = "高风险"
        elif fail_probability > 0.4:
            risk_level = "中风险"

        return {
            "code": 200,
            "probability": round(fail_probability, 4),  # 保留4位小数方便后端转百分比
            "risk_level": risk_level,
            "base_score_eval": round(base_score, 1) # 返回基础评估分供调试参考
        }

    except Exception as e:
        print(f"风险预测接口报错: {e}")
        return {"code": 500, "msg": "error", "probability": 0.0, "risk_level": "未知风险"}


# 启动方式说明：
# uvicorn main:app --reload --port 8000