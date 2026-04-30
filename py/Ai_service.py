from fastapi import FastAPI
from pydantic import BaseModel
import requests
import json
import math
import uvicorn

app = FastAPI()

# Ollama 本地 API 地址
OLLAMA_API_URL = "http://localhost:11434/api/generate"
MODEL_NAME = "qwen:1.8b"


# --- 数据模型定义 ---

class CareerRequest(BaseModel):
    student_name: str
    scores: dict  # 6维能力分 {"核心力": 85, "逻辑力": 60, ...}
    weaknesses: str  # 薄弱知识点描述
    intent: str  # job(工作), grad(考研), civil(考公)


class RadarRequest(BaseModel):
    course_name: str
    indicators: list
    scores: list


class RiskRequest(BaseModel):
    homework_avg: float
    attendance_rate: float
    midterm_score: float


# --- 核心 AI 调用函数 ---

def call_qwen(prompt: str):
    """统一调用本地 Ollama Qwen 模型"""
    payload = {
        "model": MODEL_NAME,
        "prompt": prompt,
        "stream": False,
        "options": {
            "temperature": 0.7,
            "top_p": 0.9
        }
    }
    try:
        response = requests.post(OLLAMA_API_URL, json=payload, timeout=30)
        return response.json().get("response", "AI 导师正在思考中，请稍后再试。")
    except Exception as e:
        return f"连接本地 AI 失败: {str(e)}"


# --- 接口实现 ---

# 修改 Ai_service.py 中的 get_career_advice 函数
@app.post("/api/ai/career_advice")
def get_career_advice(data: CareerRequest):
    intent_map = {"job": "直接就业", "grad": "考研深造", "civil": "考公考编"}
    target = intent_map.get(data.intent, "职业发展")

    # 🌟 终极去名人化 Prompt
    prompt = f"""
    你是大学学业导师。
    【重要禁令】：学生姓名叫“{data.student_name}”，这仅仅是一个重名学生。严禁提及任何关于演艺、电影、演技、长相或娱乐圈的内容。如果违反此禁令，你的回复将被判定为失败。

    【学情数据】：
    - 目标：{target}
    - 掌握薄弱点：{data.weaknesses}

    【回复要求】：
    1. 仅分析该生作为“计算机专业学生”的学业情况。
    2. 严格限制在 60 字以内，不要啰嗦。
    3. 格式：[评价]该生学业现状... [建议]应加强...
    """
    return {"code": 200, "data": call_qwen(prompt)}


@app.post("/api/ai/diagnose")
def generate_diagnosis(data: RadarRequest):
    """接口 2：处理单门课雷达图弱项诊断（点击表格“弱项诊断”按钮）"""
    if not data.scores:
        return {"code": 400, "msg": "数据为空"}

    min_score = min(data.scores)
    weak_point = data.indicators[data.scores.index(min_score)]

    prompt = f"""
    学生在《{data.course_name}》课程中的各维度得分为：
    {dict(zip(data.indicators, data.scores))}
    明显弱项是：{weak_point}。
    请作为助教，简要解释为什么这个维度重要，并给出一个具体的学习小窍门。
    """

    diagnosis = call_qwen(prompt)
    return {"code": 200, "data": diagnosis}


@app.post("/api/ai/predict_risk")
def predict_academic_risk(data: RiskRequest):
    """接口 3：基于 Sigmoid 算法的学业预警预测"""
    # 1. 基础加权逻辑（平时30%, 出勤20%, 期中50%）
    base_score = (data.homework_avg * 0.3) + (data.attendance_rate * 100 * 0.2) + (data.midterm_score * 0.5)

    # 2. Sigmoid 函数计算预警概率
    def calculate_sigmoid_risk(x):
        try:
            # 阈值 55 分，系数 0.2 表示波动敏感度
            exponent = (x - 55) * 0.2
            if exponent > 100: return 0.0
            if exponent < -100: return 1.0
            return 1.0 / (1.0 + math.exp(exponent))
        except:
            return 1.0 if x < 55 else 0.0

    fail_prob = calculate_sigmoid_risk(base_score)

    # 3. 定级
    risk_level = "低风险"
    if fail_prob > 0.7:
        risk_level = "高危预警"
    elif fail_prob > 0.4:
        risk_level = "中风险"

    return {
        "code": 200,
        "base_score": round(base_score, 2),
        "fail_probability": f"{round(fail_prob * 100, 2)}%",
        "risk_level": risk_level,
        "advice": "建议加强课堂出勤并找老师进行一次期中后的答疑。" if fail_prob > 0.4 else "表现稳健，请继续保持。"
    }


if __name__ == "__main__":
    print(f"本地 AI 服务已启动，正在监听 8000 端口... 模型: {MODEL_NAME}")
    uvicorn.run(app, host="0.0.0.0", port=8000)