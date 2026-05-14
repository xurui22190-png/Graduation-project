from typing import Any, Dict, List, Optional

from fastapi import FastAPI
from fastapi.responses import StreamingResponse
from pydantic import BaseModel, Field
import json
import math
import requests
import uvicorn

app = FastAPI()

OLLAMA_API_URL = "http://localhost:11434/api/generate"
MODEL_NAME = "qwen:1.8b"


class CareerRequest(BaseModel):
    student_name: str = "该生"
    student_no: Optional[str] = ""
    course_name: Optional[str] = "当前课程"
    scores: Dict[str, float] = Field(default_factory=dict)
    details: List[Dict[str, Any]] = Field(default_factory=list)
    weaknesses: str = "暂无明显薄弱项"
    strengths: str = "暂无明显优势项"
    overall_rate: Optional[float] = None
    intent: str = "job"
    intent_label: Optional[str] = None
    intent_focus: Optional[str] = None


class PlanningRequest(BaseModel):
    student_name: str = "该生"
    student_no: Optional[str] = ""
    intent: str = "job"
    intent_label: Optional[str] = None
    intent_focus: Optional[str] = None
    dimensions: Dict[str, float] = Field(default_factory=dict)
    score_summary: Dict[str, Any] = Field(default_factory=dict)
    courses: List[Dict[str, Any]] = Field(default_factory=list)


class ScoreWeaknessRequest(BaseModel):
    student_name: str = "该生"
    student_no: Optional[str] = ""
    avg_score: Optional[float] = None
    pass_rate: Optional[float] = None
    course_count: Optional[int] = None
    max_score: Optional[float] = None
    min_score: Optional[float] = None
    courses: List[Dict[str, Any]] = Field(default_factory=list)


class ClassReportRequest(BaseModel):
    class_name: str = "当前班级"
    course_name: str = "当前课程"
    student_count: int = 0
    avg_score: Optional[float] = None
    max_score: Optional[float] = None
    min_score: Optional[float] = None
    pass_rate: Optional[float] = None
    excellent_rate: Optional[float] = None
    standard_deviation: Optional[float] = None
    weakest_point: str = "暂无知识点明细"
    strongest_point: str = "暂无知识点明细"
    score_ranges: Dict[str, int] = Field(default_factory=dict)
    point_stats: List[Dict[str, Any]] = Field(default_factory=list)


class RadarRequest(BaseModel):
    course_name: str
    indicators: List[str]
    scores: List[float]
    student_name: Optional[str] = "该生"
    details: List[Dict[str, Any]] = Field(default_factory=list)
    weaknesses: str = "暂无明显薄弱项"
    strengths: str = "暂无明显优势项"
    overall_rate: Optional[float] = None


class RiskRequest(BaseModel):
    homework_avg: float
    attendance_rate: float
    midterm_score: float


def call_qwen(prompt: str) -> str:
    payload = {
        "model": MODEL_NAME,
        "prompt": prompt,
        "stream": False,
        "options": {
            "temperature": 0.35,
            "top_p": 0.85
        }
    }

    try:
        response = requests.post(OLLAMA_API_URL, json=payload, timeout=90)
        if response.status_code == 200:
            return response.json().get("response", "AI 暂无分析结果")
        return f"AI 服务异常，状态码: {response.status_code}"
    except Exception as exc:
        return f"连接本地 AI 失败: {exc}"


def call_qwen_stream(prompt: str):
    payload = {
        "model": MODEL_NAME,
        "prompt": prompt,
        "stream": True,
        "options": {
            "temperature": 0.35,
            "top_p": 0.85
        }
    }

    try:
        with requests.post(OLLAMA_API_URL, json=payload, stream=True, timeout=(10, 120)) as response:
            if response.status_code != 200:
                yield f"AI 服务异常，状态码: {response.status_code}"
                return

            for line in response.iter_lines(decode_unicode=True):
                if not line:
                    continue
                try:
                    item = json.loads(line)
                    chunk = item.get("response", "")
                    if chunk:
                        yield chunk
                    if item.get("done"):
                        break
                except json.JSONDecodeError:
                    continue
    except Exception as exc:
        yield f"连接本地 AI 失败: {exc}"


def normalize_intent(intent: str) -> str:
    if intent in {"grad", "civil"}:
        return intent
    return "job"


def intent_label(intent: str) -> str:
    intent_map = {
        "job": "就业导向",
        "grad": "考研深造导向",
        "civil": "考公考编导向"
    }
    return intent_map.get(normalize_intent(intent), "就业导向")


def intent_focus(intent: str) -> str:
    focus_map = {
        "job": "项目实践、工程实现、问题拆解、作品展示和岗位能力匹配",
        "grad": "理论基础、逻辑推导、综合题迁移、科研阅读和长期深造潜力",
        "civil": "基础稳定性、限时准确率、材料理解、规范表达和复盘纪律"
    }
    return focus_map.get(normalize_intent(intent), focus_map["job"])


def intent_rule(intent: str) -> str:
    rule_map = {
        "job": "【就业导向】改进建议必须落到项目任务、代码实现、作品集材料或实习准备，避免只讲刷题。",
        "grad": "【考研导向】改进建议必须落到概念推导、综合题训练、错题归因和复习周期，避免只讲项目实践。",
        "civil": "【考公导向】改进建议必须落到基础题稳定性、限时训练、材料理解和规范表达，避免只讲科研或项目。"
    }
    return rule_map.get(normalize_intent(intent), rule_map["job"])


def format_details(data: CareerRequest) -> str:
    if data.details:
        lines = []
        for item in data.details:
            name = item.get("name", "未命名知识点")
            rate = float(item.get("rate", 0) or 0)
            actual = float(item.get("actualScore", 0) or 0)
            max_score = float(item.get("maxScore", 0) or 0)
            level = item.get("level", "")
            lines.append(f"- {name}: 掌握率{rate:.1f}%, 得分{actual:.1f}/{max_score:.1f}, 等级{level}")
        return "\n".join(lines)

    if data.scores:
        return "\n".join([f"- {name}: 掌握率{float(rate):.1f}%" for name, rate in data.scores.items()])

    return "- 暂无知识点明细"


def format_dimensions(dimensions: Dict[str, float]) -> str:
    if not dimensions:
        return "- 暂无六维画像数据"
    return "\n".join([f"- {name}: {float(score):.1f}" for name, score in dimensions.items()])


def format_courses(courses: List[Dict[str, Any]]) -> str:
    if not courses:
        return "- 暂无课程成绩"

    lines = []
    for course in courses[:10]:
        lines.append(
            f"- {course.get('term', '')} {course.get('course_name', '未命名课程')}: "
            f"总分{float(course.get('score', 0) or 0):.1f}, "
            f"平时{float(course.get('regular_score', 0) or 0):.1f}, "
            f"测试{float(course.get('test_score', 0) or 0):.1f}, "
            f"期末{float(course.get('exam_score', 0) or 0):.1f}"
        )
    return "\n".join(lines)


def format_score_courses(courses: List[Dict[str, Any]]) -> str:
    if not courses:
        return "- 暂无课程成绩"

    lines = []
    for course in courses[:20]:
        lines.append(
            f"- {course.get('term', '')} {course.get('course_name', '未命名课程')}: "
            f"总分{float(course.get('score', 0) or 0):.1f}, "
            f"平时{float(course.get('regular_score', 0) or 0):.1f}, "
            f"测试{float(course.get('test_score', 0) or 0):.1f}, "
            f"期末{float(course.get('exam_score', 0) or 0):.1f}"
        )
    return "\n".join(lines)


def format_score_ranges(score_ranges: Dict[str, int]) -> str:
    if not score_ranges:
        return "- 暂无分数段数据"
    return "\n".join([f"- {name}: {count}人" for name, count in score_ranges.items()])


def format_class_points(point_stats: List[Dict[str, Any]]) -> str:
    if not point_stats:
        return "- 暂无知识点聚合数据"

    lines = []
    for point in point_stats[:12]:
        lines.append(
            f"- {point.get('name', '未命名知识点')}: "
            f"平均掌握率{float(point.get('avgRate', 0) or 0):.1f}%, "
            f"平均得分{float(point.get('avgActualScore', 0) or 0):.1f}/"
            f"{float(point.get('avgMaxScore', 0) or 0):.1f}, "
            f"样本{int(point.get('studentCount', 0) or 0)}人, "
            f"等级{point.get('level', '')}"
        )
    return "\n".join(lines)


def build_career_prompt(data: CareerRequest) -> str:
    detail_text = format_details(data)
    overall = "暂无" if data.overall_rate is None else f"{float(data.overall_rate):.1f}%"
    target_label = data.intent_label or intent_label(data.intent)
    target_focus = data.intent_focus or intent_focus(data.intent)
    target_rule = intent_rule(data.intent)

    return f"""
你是一名高校计算机类课程学业诊断专家，请基于给定数据生成专业、可信、可执行的学情分析报告。

【学生与课程】
- 学生：{data.student_name}
- 学号：{data.student_no or "未提供"}
- 课程：{data.course_name}
- 发展目标：{target_label}
- 目标关注：{target_focus}
- 平均掌握率：{overall}
- 优势项：{data.strengths}
- 薄弱项：{data.weaknesses}

【知识点明细】
{detail_text}

【硬性要求】
1. 只能依据上述数据分析，不得编造不存在的课程、考试、教师评价或学生经历。
2. 不要提及娱乐人物、公众人物、影视、外貌等无关内容。
3. 输出必须包含以下栏目，且栏目名完全保留：
【学情概览】
【优势表现】
【薄弱诊断】
【改进建议】
【跟踪建议】
4. 必须围绕当前发展目标生成差异化分析，不同目标不能套用同一套建议。{target_rule}
5. 语气要像教务/任课教师的专业诊断，避免空话，例如“继续努力”“加油”等。
6. 字数控制在 260 到 420 字之间，每条建议要能落到具体学习动作。
"""


@app.post("/api/career_advice")
def get_career_advice(data: CareerRequest):
    return {"code": 200, "data": call_qwen(build_career_prompt(data))}


@app.post("/api/career_advice_stream")
def stream_career_advice(data: CareerRequest):
    def event_stream():
        for chunk in call_qwen_stream(build_career_prompt(data)):
            yield f"data: {json.dumps(chunk, ensure_ascii=False)}\n\n"
        yield "event: done\ndata: true\n\n"

    return StreamingResponse(event_stream(), media_type="text/event-stream")


@app.post("/api/planning_report")
def get_planning_report(data: PlanningRequest):
    target_label = data.intent_label or intent_label(data.intent)
    target_focus = data.intent_focus or intent_focus(data.intent)
    target_rule = intent_rule(data.intent)
    summary = data.score_summary or {}

    prompt = f"""
你是一名高校学生发展规划导师，请基于学生的六维能力画像和整体成绩数据，生成未来发展路径分析。

【学生基础信息】
- 学生：{data.student_name}
- 学号：{data.student_no or "未提供"}
- 规划方向：{target_label}
- 重点关注：{target_focus}

【六维能力画像】
{format_dimensions(data.dimensions)}

【成绩总览】
- 平均分：{float(summary.get('avg_score', 0) or 0):.1f}
- 及格率：{float(summary.get('pass_rate', 0) or 0):.1f}%
- 课程数：{int(summary.get('course_count', 0) or 0)}
- 最高分：{float(summary.get('max_score', 0) or 0):.1f}
- 最低分：{float(summary.get('min_score', 0) or 0):.1f}
- 高分课程：{", ".join(summary.get('strong_courses', []) or ["暂无"])}
- 低分课程：{", ".join(summary.get('weak_courses', []) or ["暂无"])}

【课程成绩列表】
{format_courses(data.courses)}

【硬性要求】
1. 只能依据已给出的六维画像和成绩数据做分析，不得编造经历、竞赛、实习、证书、家庭背景或性格故事。
2. 这不是单门课程诊断，而是未来规划分析，重点判断学生更适合怎样的发展路径、当前准备度如何、接下来应该怎么补齐。
3. 输出必须包含以下栏目，且栏目名完全保留：
【规划概览】
【优势支撑】
【风险提醒】
【路径建议】
【行动计划】
4. 必须围绕当前规划方向给出差异化建议，不同方向不能复用同一套建议。{target_rule}
5. 分析时要同时结合六维画像和整体成绩，不要只盯着某一门课。
6. 字数控制在 280 到 450 字之间，建议要具体，可执行。
"""

    return {"code": 200, "data": call_qwen(prompt)}


@app.post("/api/score_weakness")
def get_score_weakness(data: ScoreWeaknessRequest):
    prompt = f"""
你是一名高校成绩分析导师，请只根据学生所有课程成绩，生成弱项诊断报告。

【学生信息】
- 学生：{data.student_name}
- 学号：{data.student_no or "未提供"}

【成绩总览】
- 平均分：{float(data.avg_score or 0):.1f}
- 及格率：{float(data.pass_rate or 0):.1f}%
- 课程数：{int(data.course_count or len(data.courses))}
- 最高分：{float(data.max_score or 0):.1f}
- 最低分：{float(data.min_score or 0):.1f}

【所有课程成绩】
{format_score_courses(data.courses)}

【硬性要求】
1. 只分析课程成绩本身，不要使用六维画像、就业、考研、考公或未来规划信息。
2. 必须指出低分课程、成绩波动、可能的共性弱项和优先补救顺序。
3. 输出必须包含以下栏目，且栏目名完全保留：
【成绩概览】
【弱项判断】
【原因推断】
【改进建议】
【跟踪建议】
4. 不得编造不存在的考试细节、教师评价或学生经历。
5. 字数控制在 260 到 420 字之间，建议要具体可执行。
"""

    return {"code": 200, "data": call_qwen(prompt)}


@app.post("/api/class_report")
def get_class_report(data: ClassReportRequest):
    prompt = f"""
你是一名高校任课教师与教研分析专家，请根据班级成绩和知识点聚合数据，生成班级层面的教研分析报告。

【班级与课程】
- 班级：{data.class_name}
- 课程：{data.course_name}
- 参考人数：{int(data.student_count or 0)}
- 平均分：{float(data.avg_score or 0):.1f}
- 最高分：{float(data.max_score or 0):.1f}
- 最低分：{float(data.min_score or 0):.1f}
- 及格率：{float(data.pass_rate or 0):.1f}%
- 优秀率：{float(data.excellent_rate or 0):.1f}%
- 标准差：{float(data.standard_deviation or 0):.1f}
- 集体最弱知识点：{data.weakest_point}
- 集体最强知识点：{data.strongest_point}

【分数段分布】
{format_score_ranges(data.score_ranges)}

【知识点聚合明细】
{format_class_points(data.point_stats)}

【硬性要求】
1. 只能依据上述班级成绩、分数段和知识点聚合数据分析，不得编造不存在的考试题、教师评价或课堂表现。
2. 这是班级教研报告，不是单个学生报告；重点要给任课教师可执行的复习课、分层辅导、课堂训练建议。
3. 输出必须包含以下栏目，且栏目名完全保留：
【班级概览】
【集体短板】
【优势表现】
【教学建议】
【跟踪建议】
4. 必须点名最弱知识点，并说明下一轮教学优先级。
5. 字数控制在 280 到 460 字之间，避免空泛鼓励。
"""

    return {"code": 200, "data": call_qwen(prompt)}


@app.post("/api/diagnose")
def generate_diagnosis(data: RadarRequest):
    if not data.scores:
        return {"code": 400, "msg": "数据为空"}

    min_score = min(data.scores)
    weak_point = data.indicators[data.scores.index(min_score)]
    overall = "暂无" if data.overall_rate is None else f"{float(data.overall_rate):.1f}%"

    prompt = f"""
你是一名高校课程学情分析导师，请根据学生在单门课程中的知识点掌握率，生成专业、具体的单科学情分析。

【学生与课程】
- 学生：{data.student_name or "该生"}
- 课程：{data.course_name}
- 平均掌握率：{overall}
- 优势项：{data.strengths}
- 薄弱项：{data.weaknesses}
- 最低掌握项：{weak_point}，掌握率：{min_score:.1f}%

【知识点雷达数据】
{dict(zip(data.indicators, data.scores))}

【输出要求】
1. 只能依据上述知识点掌握率分析，不得编造不存在的考试、经历或教师评价。
2. 输出必须包含以下栏目，且栏目名保留：
【学情概览】
【优势表现】
【薄弱诊断】
【学习建议】
【跟踪建议】
3. 不要只说“继续努力”，要给出具体复习动作。
4. 字数控制在 220 到 360 字。
"""

    return {"code": 200, "data": call_qwen(prompt)}


@app.post("/api/predict_risk")
def predict_academic_risk(data: RiskRequest):
    base_score = (
        data.homework_avg * 0.3
        + data.attendance_rate * 100 * 0.2
        + data.midterm_score * 0.5
    )

    def calculate_sigmoid_risk(x: float) -> float:
        try:
            exponent = (x - 55) * 0.2
            if exponent > 100:
                return 0.0
            if exponent < -100:
                return 1.0
            return 1.0 / (1.0 + math.exp(exponent))
        except Exception:
            return 1.0 if x < 55 else 0.0

    fail_prob = calculate_sigmoid_risk(base_score)

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
        "advice": "建议加强课堂出勤并安排一次针对性答疑。" if fail_prob > 0.4 else "表现稳健，请继续保持。"
    }


if __name__ == "__main__":
    print(f"本地 AI 微服务已启动，端口 8000，模型: {MODEL_NAME}")
    uvicorn.run(app, host="0.0.0.0", port=8000)
