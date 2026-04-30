package com.demo.common;

import lombok.Data;

@Data
public class ResponseResult {

    private Integer code; //编码成功：200    失败：400
    private String msg;//消息
    private Object data;//对象数据

    /*成功*/
    public static ResponseResult success(){
        return new ResponseResult(200,"success",null);
    }
    /*成功:自定义和数据*/
    public static ResponseResult success( String _msg ,Object _data){
        return new ResponseResult(200,_msg,_data);
    }

    /*失败*/
    public static ResponseResult Fail(){
        return new ResponseResult(400,"fail",null);
    }

    /*失败:自定义和数据*/
    public static ResponseResult Fail( String _msg ,Object _data){
        return new ResponseResult(400,_msg,_data);
    }

    /*失败:自定义和数据*/
    public static ResponseResult Fail( String _msg ){
        return new ResponseResult(400,_msg,null);
    }

    /*完全自定义*/
    public static ResponseResult ResResult(Integer _code  ,String _msg ,Object _data){
        return new ResponseResult(_code,_msg,_data);
    }
    /*自定义状态值*/
    public static ResponseResult ResResult(boolean _statys,Object _data){
        return new ResponseResult(_statys?200:400, _statys?"success":"fail",_data);
    }

    public static ResponseResult ResResult(boolean _statys){
        return new ResponseResult(_statys?200:400, _statys?"success":"fail",null);
    }
    /*完整自定义*/
    public static ResponseResult ResResult(boolean _statys,String _msg ,Object _data ){
        return new ResponseResult(_statys? 200: 400 , _msg, _data);
    }

    public ResponseResult(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
