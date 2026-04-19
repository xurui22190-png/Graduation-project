package com.demo.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ResponsePageResult<T> {

    private Integer _code;
    private String _msg;
    private List<T> data = null;
    private long totalRecord = 0L;
    private long totalPage;
    private long pageIndex;

    public ResponsePageResult(Integer _code, String _msg, IPage _pagedata) {
        this._code = _code;
        this._msg = _msg;
        this.data = _pagedata.getRecords();
        this.totalRecord = _pagedata.getTotal();
        this.totalPage = _pagedata.getPages();
        this.pageIndex = _pagedata.getCurrent();
    }

    public static ResponsePageResult PageResult(IPage _pagedata) {
        return new ResponsePageResult(200, "success", _pagedata);
    }

    public static ResponsePageResult ResResult(Integer _code, String _msg, IPage _pagedata) {
        return new ResponsePageResult(_code, _msg, _pagedata);
    }
}