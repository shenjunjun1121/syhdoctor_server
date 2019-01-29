package com.syhdoctor.webserver.controller.webadmin.prescription;

import com.syhdoctor.common.utils.ModelUtil;
import com.syhdoctor.common.utils.StrUtil;
import com.syhdoctor.webserver.base.controller.BaseController;
import com.syhdoctor.webserver.service.prescription.PrescriptionService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(description = "/Admin/Prescription 电子处方")
@RestController
@RequestMapping("/Admin/Prescription")
public class AdminPrescriptionController extends BaseController {

    @Autowired
    private PrescriptionService prescriptionService;

    @ApiOperation(value = "处方列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "username", value = "患者姓名", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "doctorname", value = "医生姓名", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "diagnosis", value = "诊断", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "examine", value = "审核状态", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pageindex", value = "分页", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pagesixe", value = "分页", dataType = "String"),
    })
    @PostMapping("/getPrescriptionList")
    public Map<String, Object> getPrescriptionList(@ApiParam(hidden = true) @RequestParam Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        String userName = ModelUtil.getStr(params, "ptno");
        String doctorName = ModelUtil.getStr(params, "doctorname");
        String diagnosis = ModelUtil.getStr(params, "diagnosis");
        int examine = ModelUtil.getInt(params, "examine");
        int pageIndex = ModelUtil.getInt(params, "pageindex", 1);
        int pageSize = ModelUtil.getInt(params, "pagesize", 20);
        result.put("data", prescriptionService.getPrescriptionList(examine, userName, doctorName, diagnosis, pageIndex, pageSize));
        setOkResult(result, "查询成功");
        return result;
    }

    @ApiOperation(value = "处方详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "处方id", dataType = "String"),
    })
    @PostMapping("/getPrescription")
    public Map<String, Object> getPrescription(@ApiParam(hidden = true) @RequestParam Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        long id = ModelUtil.getLong(params, "id");
        if (id == 0) {
            setErrorResult(result, "参数错误");
        } else {
            result.put("data", prescriptionService.getAdminPrescription(id));
            setOkResult(result, "查询成功");
        }
        return result;
    }

    @ApiOperation(value = "预览处方")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "处方id", dataType = "String"),
    })
    @PostMapping("/addPrescription")
    public Map<String, Object> addPrescription(@ApiParam(hidden = true) @RequestParam Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        long doctorId = ModelUtil.getLong(params, "doctorid");
        long orderId = ModelUtil.getLong(params, "orderid");
        String diagnosis = ModelUtil.getStr(params, "diagnosis");
        List<?> druglist = ModelUtil.getList(params, "druglist", new ArrayList<>());
        if (doctorId == 0 || orderId == 0) {
            setErrorResult(result, "参数错误");
        } else {
            result.put("data", prescriptionService.preview(orderId, doctorId, diagnosis, druglist));
            setOkResult(result, "查询成功");
        }
        return result;

    }

    @ApiOperation(value = "药品详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "药品id", required = true, dataType = "String"),
    })
    @PostMapping("/getDrugUseDetail")
    public Map<String, Object> getDrugUseDetail(@ApiParam(hidden = true) @RequestParam Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        long id = ModelUtil.getLong(params, "id");
        result.put("data", prescriptionService.getDrugUseDetail(id));
        setOkResult(result, "ok");
        return result;
    }

    @ApiOperation(value = "药品")
    @PostMapping("/getDrugsPackageList")
    public Map<String, Object> getDrugsPackageList() {
        Map<String, Object> result = new HashMap<>();
        result.put("data", prescriptionService.getDrugsPackageList());
        setOkResult(result, "查询成功");
        return result;
    }


    @ApiOperation(value = "父-药品")
    @PostMapping("/getDrugsList")
    public Map<String, Object> getDrugsList() {
        Map<String, Object> result = new HashMap<>();
        result.put("data", prescriptionService.getDrugsList());
        setOkResult(result, "查询成功");
        return result;
    }


    @ApiOperation(value = "子-药品")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "父级id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pageindex", value = "开始页", defaultValue = "1", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pagesize", value = "每页条数", defaultValue = "20", dataType = "String"),
    })
    @PostMapping("/getMiddleDrugsList")
    public Map<String, Object> getMiddleDrugsList(@ApiParam(hidden = true) @RequestParam Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        long pid = ModelUtil.getLong(params, "id");
        String name = ModelUtil.getStr(params, "name");
        int pageIndex = ModelUtil.getInt(params, "pageindex", 1);
        int pageSize = ModelUtil.getInt(params, "pagesize", 20);
        result.put("data", prescriptionService.getMiddleDrugsList(name, pid, pageIndex, pageSize));
        result.put("total", prescriptionService.getMiddleDrugsListCount(name, pid));
        setOkResult(result, "查询成功");
        return result;
    }

    @ApiOperation(value = "处方审核")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "处方id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "examine", value = "审核状态(1:审核中,2:审核成功,3:审核失败)", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "remark", value = "失败原因", dataType = "String"),
    })
    @PostMapping("/prescriptionExamine")
    public Map<String, Object> prescriptionExamine(@ApiParam(hidden = true) @RequestParam Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        long id = ModelUtil.getLong(params, "id");
        int examine = ModelUtil.getInt(params, "examine", 3);
        String remark = ModelUtil.getStr(params, "remark");
        if (id == 0 || examine == 0) {
            setErrorResult(result, "参数错误");
            return result;
        }
        if (examine == 3 && StrUtil.isEmpty(remark)) {
            setErrorResult(result, "参数错误");
            return result;
        }
        prescriptionService.prescriptionExamine(id, examine, remark);
        setOkResult(result, "查询成功");
        return result;
    }
}
