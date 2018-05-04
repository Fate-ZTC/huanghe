package com.parkbobo.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.parkbobo.VO.FirePatrolEquipmentStatusVO;
import com.parkbobo.VO.FirePatrolEquipmentVO;
import com.parkbobo.VO.ModifyFireEquipmentVO;
import com.parkbobo.dao.FireFightEquipmentDao;
import com.parkbobo.dao.FireFightEquipmentHistoryDao;
import com.parkbobo.model.FireFightEquipment;
import com.parkbobo.model.FireFightEquipmentHistory;
import com.parkbobo.model.FirePatrolConfig;
import com.parkbobo.utils.Configuration;
import com.parkbobo.utils.HttpRequest;

import static com.alibaba.fastjson.JSON.parseObject;

/**
 * Created by lijunhong on 18/4/8.
 * 消防设备和专题图同步相关接口
 */
@Service
public class FirePatrolEquipmentSychService {


    public static final String CDLQURL = Configuration.getInstance().getValue("FirePatrolEquipmentSych");
    public static final String FIRE_PATROL_UPATE_STATUS_URL = Configuration.getInstance().getValue("FirePatrolEquiStatusSych");

    @Resource
    private FirePatrolConfigService firePatrolConfigService;

    @Resource(name="fireFightEquipmentDaoImpl")
    private FireFightEquipmentDao fireFightEquipmentDao;
    @Resource
    private FireFightEquipmentHistoryDao fireFightEquipmentHistoryDao;


    /**
     * 进行添加设备操作
     * @param modifyFireEquipmentVO     消防设备参数对象
     * @return
     */
    public boolean addFirePatrolEquipment(ModifyFireEquipmentVO modifyFireEquipmentVO) {
        //进行修改操作
        try {
            FireFightEquipment fireFightEquipment = new FireFightEquipment();
            FirePatrolEquipmentVO equipmentVO = modifyFireEquipmentVO.getEquipmentVO();
            fireFightEquipment.setName(equipmentVO.getName());
            fireFightEquipment.setFloorid(equipmentVO.getFloorid());
            fireFightEquipment.setPointid(equipmentVO.getPointid());

            //这里进行判断是否
            String lonLatStr = equipmentVO.getCoordinate();
            if(lonLatStr != null && !"".equals(lonLatStr)) {
                String[] lonLat = lonLatStr.replace("[","").replace("]","").split(",");
                if(lonLat != null && lonLat.length == 2) {
                    fireFightEquipment.setLon(Float.parseFloat(lonLat[0]));
                    fireFightEquipment.setLat(Float.parseFloat(lonLat[1]));
                }
            }
            if(0 != equipmentVO.getLon()) {
                fireFightEquipment.setLon(equipmentVO.getLon());
            }
            if(0 != equipmentVO.getLat()) {
                fireFightEquipment.setLat(equipmentVO.getLat());
            }
            fireFightEquipment.setLastUpdateTime(new Date());
            fireFightEquipment.setIcon(equipmentVO.getIcon());
            //设置正常
            fireFightEquipment.setStatus((short)0);
            //设置为未巡查状态
            fireFightEquipment.setCheckStatus((short)0);
            //设置校区
            fireFightEquipment.setCampusNum(1);
            fireFightEquipmentDao.save(fireFightEquipment);

            //TODO 这里要进行测试（这里进行了增加操作，需要进行测试）

            //将上面添加的数据查询出来
            List<FireFightEquipment> fireFightEquipments = fireFightEquipmentDao.getByProperty("pointid",fireFightEquipment.getPointid());
            if(fireFightEquipments != null && fireFightEquipments.size() > 0) {
                FireFightEquipment entity = fireFightEquipments.get(0);
                //将设备同时添加到设备历史记录表中
                FireFightEquipmentHistory fireFightEquipmentHistory = new FireFightEquipmentHistory();
                fireFightEquipmentHistory.setName(fireFightEquipment.getName());
                fireFightEquipmentHistory.setLastUpdateTime(new Date());
                fireFightEquipmentHistory.setLon(fireFightEquipment.getLon());
                fireFightEquipmentHistory.setLat(fireFightEquipment.getLat());
                fireFightEquipmentHistory.setCheckStatus((short) 0);
                //这里设置为默认校区为1
                fireFightEquipmentHistory.setCampusNum(1);
                fireFightEquipmentHistory.setOldId(entity.getId());

                fireFightEquipmentHistory.setFloorid(fireFightEquipment.getFloorid());
                if(fireFightEquipment.getBuildingCode() != null) {
                    fireFightEquipmentHistory.setBuildingCode(fireFightEquipment.getBuildingCode());
                }
                fireFightEquipmentHistoryDao.save(fireFightEquipmentHistory);
            }
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 根据pointid进行删除操作
     * @param pointid   同步id
     * @return
     */
    public int deleteFirePatrolEquipment(int pointid) {
        //这里进行查询
        boolean isExist = fireFightEquipmentDao.existsByProperty("pointid",pointid);
        if(!isExist) {
            //不存在
            return 0;
        }else {

            String hql = "DELETE FROM FireFightEquipment WHERE pointid=" + pointid;
            int result = fireFightEquipmentDao.deleteByHql(hql);
            if(result > 0) {
                return 1;
            }else {
                return 0;
            }
        }
    }


    /**
     * 进行更新操作
     * @param modifyFireEquipmentVO     更新实体相关内容
     * @return
     */
    public boolean modifyFirePatrolEquipment(ModifyFireEquipmentVO modifyFireEquipmentVO) {
        //进行查询操作
        FireFightEquipment fightEquipment = fireFightEquipmentDao.getUniqueByProperty("pointid",modifyFireEquipmentVO.getPointid());

        FireFightEquipment entity = new FireFightEquipment();
        entity.setId(fightEquipment.getId());
        entity.setName(fightEquipment.getName());
        entity.setLastUpdateTime(new Date());
        entity.setLon(fightEquipment.getLon());
        entity.setLat(fightEquipment.getLat());
        entity.setStatus(fightEquipment.getStatus());
        entity.setCheckStatus(fightEquipment.getCheckStatus());
        entity.setCampusNum(fightEquipment.getCampusNum());
        entity.setFloorid(fightEquipment.getFloorid());
        entity.setIcon(fightEquipment.getIcon());
        entity.setPointid(fightEquipment.getPointid());
        FirePatrolEquipmentVO equipmentVO  = modifyFireEquipmentVO.getEquipmentVO();
        try {
            if (fightEquipment != null && !"".equals(fightEquipment)) {
                //查询到数据
                if (null != equipmentVO.getName() && !"".equals(equipmentVO.getName())) {
                    fightEquipment.setName(equipmentVO.getName());
                }
                if (null != equipmentVO.getFloorid() && !"".equals(equipmentVO.getFloorid())) {
                    fightEquipment.setFloorid(equipmentVO.getFloorid());
                }
                if (0 != equipmentVO.getLon()) {
                    fightEquipment.setLon(equipmentVO.getLon());
                }
                if (0 != equipmentVO.getLat()) {
                    fightEquipment.setLat(equipmentVO.getLat());
                }
                if (0 != equipmentVO.getPointid()) {
                    fightEquipment.setPointid(equipmentVO.getPointid());
                }
                //这里进行更新操作
                fireFightEquipmentDao.merge(fightEquipment);
                return true;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }





    /**
     * 进行数据同步消防设备
     */
    public void saveFirePatrolEquipment() {
        String midParamValue = "";
        //进行获取相关消防设备配置信息
        FirePatrolConfig firePatrolConfig = firePatrolConfigService.getById(1);
        if(firePatrolConfig != null && firePatrolConfig.getEquipmentType() == null) {
            //设置一个默认值
            midParamValue = "301";
        }else {
            midParamValue = firePatrolConfig.getEquipmentType();
        }

        try {
            List<FirePatrolEquipmentVO> vos = getFirePatrolEquipment(midParamValue);
            List<FirePatrolEquipmentVO> voList = formatFirePatrolEquipment(vos);
            List<FireFightEquipment> fightEquipments = new ArrayList<>((voList != null && voList.size() > 0)?voList.size():10);
            //进行设置
            for (FirePatrolEquipmentVO vo : voList) {
                //进行设置相关内容
                FireFightEquipment fightEquipment = new FireFightEquipment();

                String[] lonLat = null;
                //这里进行处理经纬度
                if(vo.getCoordinate() != null) {
                    lonLat = vo.getCoordinate().trim().replace("[","").replace("]","").split(",");
                    if(lonLat != null && lonLat.length == 2) {
                        fightEquipment.setLon(Float.parseFloat(lonLat[0]));
                        fightEquipment.setLat(Float.parseFloat(lonLat[1]));
                    }
                }
                if(vo.getThematicPointCategory() != null) {
                    fightEquipment.setName(vo.getThematicPointCategory().getName());
                    fightEquipment.setCategoryid(vo.getThematicPointCategory().getCategoryid());
                }
                //这里进行设置默认校区id
                fightEquipment.setCampusNum(1);
                fightEquipment.setFloorid(vo.getFloorid());
                fightEquipment.setIcon(vo.getIcon());
                //设置同步库的id
                fightEquipment.setPointid(vo.getPointid());
                fightEquipment.setLastUpdateTime(new Date());
                //设置消防设备(同步时默认正常)
                fightEquipment.setStatus((short)1);
                //设置是否检查(默认未检查)
                fightEquipment.setCheckStatus((short)0);
                //同步的时候进行设置大楼id
                fightEquipment.setBuildingCode(vo.getBuildingCode());


                boolean isExist = fireFightEquipmentDao.existsByProperty("pointid",vo.getPointid());
                if(!isExist) {
                    //进行查询是否存在
                    fightEquipments.add(fightEquipment);
                }
            }
            //这里进行批量保存
            fireFightEquipmentDao.bulksave(fightEquipments);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 通过请求接口获取川师消防专题图相关设备信息
     * @param param     参数301
     * @return
     * @throws Exception
     */
    public List<FirePatrolEquipmentVO> getFirePatrolEquipment(String param) throws Exception{
        HttpRequest httpRequest = new HttpRequest();
        if(CDLQURL == null && "".equals(CDLQURL) && param == null && "".equals(param)) {return null;}

        String params = "mid=" + param;
        String result = httpRequest.sendGet(CDLQURL,params);
        if(result != null && !"".equals(result)) {
            JSONObject jsonObject = parseObject(result);
            if(jsonObject != null && !"".equals(jsonObject)) {
                Integer code = jsonObject.getInteger("code");
                if(code == 200) {
                    System.out.println(JSON.parseArray(jsonObject.getString("data"),FirePatrolEquipmentVO.class));
                    return JSON.parseArray(jsonObject.getString("data"), FirePatrolEquipmentVO.class);
                }
            }
        }
        return null;
    }

    /**
     * 对数据进行格式化
     * @param vos
     * @return
     */
    public List<FirePatrolEquipmentVO> formatFirePatrolEquipment(List<FirePatrolEquipmentVO> vos) {
        if(vos != null && vos.size() > 0) {
            for(FirePatrolEquipmentVO vo : vos) {
                String[] lonLat = vo.getCoordinate()
                        .replace("[","")
                        .replace("]","")
                        .split(",");
                //进行设置相关内容
                if(lonLat.length > 2) {
                    //设置经度纬度
                    vo.setLon(Float.parseFloat(lonLat[0]));
                    vo.setLat(Float.parseFloat(lonLat[1]));
                }
            }
        }
        return vos;
    }


    /**
     * 巡查时和专题图进行状态更新
     * @param statusVO  跟新对象
     * @return 是否跟新成功
     */
    public boolean updateFirePatrolEquipmentVOStatus(FirePatrolEquipmentStatusVO statusVO) {
        HttpRequest request = new HttpRequest();
        if(statusVO != null && !"".equals(statusVO)) {
            String json = JSON.toJSONString(statusVO);
            StringBuffer sb = new StringBuffer();
            if(statusVO.getDeviceId() != null && !"".equals(statusVO.getDeviceId())) {
                sb.append("deviceId="+statusVO.getDeviceId());
            }
            if(statusVO.getPatrolUser() != null && !"".equals(statusVO.getPatrolUser())) {
                sb.append("&patrolUser="+statusVO.getPatrolUser());
            }
            if(statusVO.getDeviceStatus() != null && !"".equals(statusVO.getDeviceStatus())) {
                sb.append("&deviceStatus="+statusVO.getDeviceStatus());
            }
            try {
                String result = request.postPort(FIRE_PATROL_UPATE_STATUS_URL,sb.toString());
                if(result != null && !"".equals(result)) {
                    JSONObject resultObject = JSONObject.parseObject(result);
                    int code = resultObject.getInteger("code");
                    if(code == 200) {
                        return true;
                    }
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
