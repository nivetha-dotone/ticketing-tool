package com.dot1.ticket_track.services;

import com.dot1.ticket_track.entity.mClientMaster;
import com.dot1.ticket_track.entity.mClientSPOCMaster;
import com.dot1.ticket_track.entity.mCompanyMaster;
import com.dot1.ticket_track.repository.SpocMstRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClientSPOCservices {
    @Autowired
private SpocMstRepo mstRepo;

    public mClientSPOCMaster createClientspoc(mClientSPOCMaster client) {
        try{
            if(client!=null){
                if(client.getIsActive()==null){
                    client.setIsActive(true);
                }
                client.setSpocId(mstRepo.newIdSPOC());
                mClientSPOCMaster save = mstRepo.save(client);
                return save;
            }else{
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }






    public List<mClientSPOCMaster> getAllClientspoc() {
       try{
           List<mClientSPOCMaster> allmClientSPOCmaster = mstRepo.findAll();
           List<mClientSPOCMaster> passedAll=new ArrayList<>();
           for(mClientSPOCMaster checked: allmClientSPOCmaster ){
               mClientMaster clientId = checked.getClientmasterId();
               mClientMaster fitnew=new mClientMaster();
                if(clientId!=null){
                    fitnew.setClientId(clientId.getClientId());
                    fitnew.setClientCode(clientId.getClientCode());
                    fitnew.setClientName(clientId.getClientName());
                    fitnew.setClientGroup(clientId.getClientGroup());
                    fitnew.setClientModule(clientId.getClientModule());
                    fitnew.setIsactive(clientId.getIsactive());
                    fitnew.setEmployeeClient(clientId.getEmployeeClient());
                    checked.setClientmasterId(fitnew);
                    passedAll.add(checked);
                }

           }
            if(passedAll!=null){
                return passedAll;
            }else{
                return null;
            }
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
    }

    public Optional<mClientSPOCMaster> getClientSpocById(Integer id) {
       try{
           return mstRepo.findById(id);

       } catch (Exception e) {
           throw new RuntimeException(e);
       }
    }

    @Autowired
    private ClientService clientService;
    public List<mClientSPOCMaster> getClientById(Integer id) {
       try{
           mClientMaster mClientMaster = clientService.getClientById(id);
           List<mClientSPOCMaster> byclientId = mstRepo.findByclientmasterId(mClientMaster).orElse(null);
                if(byclientId!=null){
                    List<mClientSPOCMaster> pass =new ArrayList<>();
                    for(mClientSPOCMaster checked: byclientId){
                        mClientMaster clientId = checked.getClientmasterId();
                        if(clientId!=null){
                            mClientMaster fitnew=new mClientMaster();
                            fitnew.setClientId(clientId.getClientId());
                            fitnew.setClientCode(clientId.getClientCode());
                            fitnew.setClientName(clientId.getClientName());
                            fitnew.setClientGroup(clientId.getClientGroup());
                            fitnew.setClientModule(clientId.getClientModule());
                            fitnew.setIsactive(clientId.getIsactive());
                            fitnew.setEmployeeClient(clientId.getEmployeeClient());
                            fitnew.setMClientSPOCMasters(null);
                            mCompanyMaster companyMaster = clientId.getCompanyMaster();
                            if(companyMaster!=null){
                                companyMaster.setMModulesMasters(null);
                                companyMaster.setClientMasters(null);
                                fitnew.setCompanyMaster(companyMaster);
                            }
                            fitnew.setCompanyMaster(companyMaster);
                            checked.setClientmasterId(fitnew);
                        }
                        pass.add(checked);
                    }
                    return pass;
                }else{
                    return null;
                }
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
    }

    public mClientSPOCMaster getbySPCOname(String name) {
       try{
           mClientSPOCMaster mClientSPOCMaster = mstRepo.findByspocName(name).orElse(null);
           if(mClientSPOCMaster!=null){
                    mClientSPOCMaster.getClientmasterId().setMClientSPOCMasters(null);
                    mClientSPOCMaster.getClientmasterId().getCompanyMaster().setClientMasters(null);
                    mClientSPOCMaster.getClientmasterId().getCompanyMaster().setMModulesMasters(null);


                    return mClientSPOCMaster;
                }else{
                    return null;
                }
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
    }


//    public mClientMaster updateClient(Integer id, mClientMaster updatedClient) {
//        try{
//            mClientMaster mClientMasterOld = clientRepository.findById(id).orElse(null);
//            if(mClientMasterOld!=null){
////                mClientMasterOld.setClientCode(updatedClient.getClientCode());
//                mClientMasterOld.setClientNs(updatedClient.getClientNs());
//                mClientMasterOld.setClientGroup(updatedClient.getClientGroup());
//                mClientMasterOld.setClientModule(updatedClient.getClientModule());
//                mClientMasterOld.setIsActive(updatedClient.getIsactive());
//                mClientMasterOld.setCompanyMaster(updatedClient.getCompanyMaster());
//                mClientMaster save = clientRepository.save(mClientMasterOld);
//                return save;
//
//            }else{
//                return null;
//            }
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//    public mClientMaster updateByclientCode(String clientCode, mClientMaster updatedClient) {
//        try{
//            mClientMaster mClientMasterOld = clientRepository.findByclientCode(clientCode).orElse(null);
//            if(mClientMasterOld!=null){
////                mClientMasterOld.setClientCode(updatedClient.getClientCode());
//                mClientMasterOld.setClientNs(updatedClient.getClientNs());
//                mClientMasterOld.setClientGroup(updatedClient.getClientGroup());
//                mClientMasterOld.setClientModule(updatedClient.getClientModule());
//                mClientMasterOld.setIsActive(updatedClient.getIsactive());
//                mClientMasterOld.setCompanyMaster(updatedClient.getCompanyMaster());
//                mClientMaster save = clientRepository.save(mClientMasterOld);
//                return save;
//
//            }else{
//                return null;
//            }
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public boolean deleteClient(Integer id) {
//       try{
//           mClientMaster mClientMaster = getClientById(id).orElse(null);
//           if(mClientMaster!=null){
//               clientRepository.deleteById(id);
//               return true;
//           }else{
//               return false;
//           }
//
//       } catch (Exception e) {
//           throw new RuntimeException(e);
//       }
//    }


//    @Transactional
//    public mClientMaster updateClientIsActive(Integer clientId, Boolean isActive) {
//        try{
//        mClientMaster client = clientRepository.findById(clientId)
//                .orElseThrow(() -> new RuntimeException("Client not found with ID: " + clientId));
//
//        client.setIsActive(isActive);
//        return clientRepository.save(client);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
}

