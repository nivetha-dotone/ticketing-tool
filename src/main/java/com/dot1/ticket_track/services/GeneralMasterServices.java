package com.dot1.ticket_track.services;


import com.dot1.ticket_track.entity.mGeneralMaster;
import com.dot1.ticket_track.repository.GeneralMstRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class GeneralMasterServices {

    @Autowired
    private GeneralMstRepo mstRepo;

    //addNew
    @Transactional
    public mGeneralMaster addGeneralMaster(mGeneralMaster generalMaster){
        try{
            String gmType = generalMaster.getGmType();
            List<mGeneralMaster> findByname = getFindByname(gmType);
            boolean topass= false;
            for(mGeneralMaster checked: findByname ){

                String gmDescription = checked.getGmDescription();

                if(gmDescription.equals(generalMaster.getGmDescription())){
                    topass=true;
                }
            }
            if(!topass){

                mGeneralMaster  save = mstRepo.save(generalMaster);
                return save;
            }else{
                return  null;
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    //    get all
    @Transactional
    public  List<mGeneralMaster> getAllGeneralMaster(){
        try{
            List<mGeneralMaster> allMaster = mstRepo.findAll();
            if(allMaster!=null){
              return allMaster;
            }else {
                return null;
            }

        } catch (Exception e) {
            return null;
        }


    }

    //    get all
    @Transactional
    public mGeneralMaster getbyidGeneralMaster(Integer id){
        try{
            mGeneralMaster mGeneralMaster = mstRepo.findById(id).orElse(null);


            if(mGeneralMaster!=null){
                return mGeneralMaster;
            }else {
                return null;
            }

        } catch (Exception e) {
            return null;
        }


    }
    @Transactional
    public Boolean gdeletebyid(Integer id){
        try{
            mGeneralMaster mGeneralMaster = mstRepo.findById(id).orElse(null);


            if(mGeneralMaster!=null){
                mstRepo.deleteById(id);
                return true;

            }else {

                return false;
            }

        } catch (Exception e) {
            return null;
        }


    }

    @Transactional
    public List<mGeneralMaster> getFindByname(String gmType){
        Optional<List<mGeneralMaster>> bygmType = mstRepo.findBygmType(gmType);
//        mGeneralMaster mGeneralMaster = bygmType.orElse(null);
        List<mGeneralMaster> mGeneralMasters = bygmType.orElse(null);
//        String byname = mGeneralMaster.getGmType();

        if(mGeneralMasters!=null){
            return mGeneralMasters;
        }else{
            return null;
        }


    }

}
