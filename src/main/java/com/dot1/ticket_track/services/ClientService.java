package com.dot1.ticket_track.services;

import com.dot1.ticket_track.entity.*;
import com.dot1.ticket_track.repository.ClientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;

    public mClientMaster createClient(mClientMaster client) {
        try{
            if(client!=null){
                if(client.getIsactive()==null){
                    client.setIsactive(false);
                }
                String s = clientRepository.newIDClientcode();
                String clientCode = "Client_" +s;

                client.setClientCode(clientCode);
                client.setClientId(clientRepository.newIdclient());
                mClientMaster save = clientRepository.save(client);
                return save;


            }else{
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<mClientMaster> getAllClients() {
       try{
           List<mClientMaster> getAll = clientRepository.findAll();
           List<mClientMaster> passedAll=new ArrayList<>();
           for(mClientMaster checked: getAll ){
               mCompanyMaster companyMaster = checked.getCompanyMaster();
               if(companyMaster!=null){
                   mCompanyMaster fitnew = new mCompanyMaster();
                   fitnew.setCmpid(companyMaster.getCmpid());
                   fitnew.setCmpcode(companyMaster.getCmpcode());
                   fitnew.setCmpnm(companyMaster.getCmpnm());

                   checked.setCompanyMaster(fitnew);
               }
               List<mClientSPOCMaster> mClientSPOCMasters = checked.getMClientSPOCMasters();
               if(mClientSPOCMasters!=null){
                   List<mClientSPOCMaster> firnewspoc= new ArrayList<>();
                   for (mClientSPOCMaster checkspoc :mClientSPOCMasters )
                   {
                       checkspoc.setClientmasterId(null);
                       firnewspoc.add(checkspoc);
                   }
                   checked.setMClientSPOCMasters(firnewspoc);
               }
               List<mClientCMEMaster> mClientCMEMasterList = checked.getMClientCMEMasterList();
               if(mClientCMEMasterList!=null){
                   List<mClientCMEMaster> fitnewcme =new ArrayList<>();
                   for(mClientCMEMaster checkedcme: mClientCMEMasterList){
                       checkedcme.setClientMasterIdCme(null);
                       mModulesMaster cmemodulesMaster = checkedcme.getCmemodulesMaster();
                       if(cmemodulesMaster!=null){
                           mModulesMaster fitnewmod = new mModulesMaster();
                           fitnewmod.setModId(cmemodulesMaster.getModId());
                           fitnewmod.setModcode(cmemodulesMaster.getModcode());
                           fitnewmod.setIsactive(cmemodulesMaster.getIsactive());
                           fitnewmod.setCompanyMaster(null);
                           fitnewmod.setEmployeeMasterList(null);
                           fitnewmod.setMClientCMEMasterList(null);
                           checkedcme.setCmemodulesMaster(fitnewmod);
                       }
//                       checkedcme.setCmemodulesMaster(cmemodulesMaster);
                        fitnewcme.add(checkedcme);


                   }
                    checked.setMClientCMEMasterList(fitnewcme);
               }




               passedAll.add(checked);

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

    public List<mClientMaster> getAllCMEPresntClients() {
       try{
           List<mClientMaster> getAll = clientRepository.findAll();
           List<mClientMaster> passedAll=new ArrayList<>();
           for(mClientMaster checked: getAll ){
                {
                   mCompanyMaster companyMaster = checked.getCompanyMaster();
                   if (companyMaster != null) {
                       mCompanyMaster fitnew = new mCompanyMaster();
                       fitnew.setCmpid(companyMaster.getCmpid());
                       fitnew.setCmpcode(companyMaster.getCmpcode());
                       fitnew.setCmpnm(companyMaster.getCmpnm());
                       checked.setCompanyMaster(fitnew);
                   }
                   List<mClientSPOCMaster> mClientSPOCMasters = checked.getMClientSPOCMasters();
                   if (mClientSPOCMasters != null) {
                       List<mClientSPOCMaster> firnewspoc = new ArrayList<>();
                       for (mClientSPOCMaster checkspoc : mClientSPOCMasters) {
                           checkspoc.setClientmasterId(null);
                           firnewspoc.add(checkspoc);
                       }
                       checked.setMClientSPOCMasters(firnewspoc);
                   }
                   List<mClientCMEMaster> mClientCMEMasterList = checked.getMClientCMEMasterList();
                   if (mClientCMEMasterList != null) {
                       List<mClientCMEMaster> fitnewcme = new ArrayList<>();
                       for (mClientCMEMaster checkedcme : mClientCMEMasterList) {

                           checkedcme.setClientMasterIdCme(null);
                           mModulesMaster cmemodulesMaster = checkedcme.getCmemodulesMaster();
                           if (cmemodulesMaster != null) {
                               mModulesMaster fitnewmod = new mModulesMaster();
                               fitnewmod.setModId(cmemodulesMaster.getModId());
                               fitnewmod.setModcode(cmemodulesMaster.getModcode());
                               fitnewmod.setIsactive(cmemodulesMaster.getIsactive());
                               fitnewmod.setCompanyMaster(null);
                               fitnewmod.setEmployeeMasterList(null);
                               fitnewmod.setMClientCMEMasterList(null);
                               checkedcme.setCmemodulesMaster(fitnewmod);
                           }
//                       checkedcme.setCmemodulesMaster(cmemodulesMaster);
//                           if(checkedcme.getIsActive()){
                               fitnewcme.add(checkedcme);



                       }
                       checked.setMClientCMEMasterList(fitnewcme);
                       if(fitnewcme!=null && !fitnewcme.isEmpty()){

                           passedAll.add(checked);
                       }
                   }


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

    public mClientMaster getClientPresentCMEByID(Integer id) {
        try {
            mClientMaster checked = clientRepository.findById(id).orElse(null);
            if (checked != null) {
                mCompanyMaster companyMaster = checked.getCompanyMaster();
                if (companyMaster != null) {
                    mCompanyMaster fitnew = new mCompanyMaster();
                    fitnew.setCmpid(companyMaster.getCmpid());
                    fitnew.setCmpcode(companyMaster.getCmpcode());
                    fitnew.setCmpnm(companyMaster.getCmpnm());

                    checked.setCompanyMaster(fitnew);
                }
                List<mClientSPOCMaster> mClientSPOCMasters = checked.getMClientSPOCMasters();
                if (mClientSPOCMasters != null) {
                    List<mClientSPOCMaster> firnewspoc = new ArrayList<>();
                    for (mClientSPOCMaster checkspoc : mClientSPOCMasters) {
                        checkspoc.setClientmasterId(null);
                        firnewspoc.add(checkspoc);
                    }
                    checked.setMClientSPOCMasters(firnewspoc);
                }
                List<mClientCMEMaster> mClientCMEMasterList = checked.getMClientCMEMasterList();
                if (mClientCMEMasterList != null) {
                    List<mClientCMEMaster> fitnewcme = new ArrayList<>();
                    for (mClientCMEMaster checkedcme : mClientCMEMasterList) {

                        checkedcme.setClientMasterIdCme(null);
                        mModulesMaster cmemodulesMaster = checkedcme.getCmemodulesMaster();
                        if (cmemodulesMaster != null) {
                            mModulesMaster fitnewmod = new mModulesMaster();
                            fitnewmod.setModId(cmemodulesMaster.getModId());
                            fitnewmod.setModcode(cmemodulesMaster.getModcode());
                            fitnewmod.setIsactive(cmemodulesMaster.getIsactive());
                            fitnewmod.setCompanyMaster(null);
                            fitnewmod.setEmployeeMasterList(null);
                            fitnewmod.setMClientCMEMasterList(null);
                            checkedcme.setCmemodulesMaster(fitnewmod);
                        }
//                        checkedcme.setCmemodulesMaster(cmemodulesMaster);
                        fitnewcme.add(checkedcme);


                    }
                    checked.setMClientCMEMasterList(fitnewcme);



                }
                if(checked.getMClientCMEMasterList()!=null && !checked.getMClientCMEMasterList().isEmpty()) {
                    return checked;

                }else{
                    return null;
                }

            }else{
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public mClientMaster getClientPresentActiveCMEByID(Integer id) {
        try {
            mClientMaster checked = clientRepository.findById(id).orElse(null);
            if (checked != null) {
                mCompanyMaster companyMaster = checked.getCompanyMaster();
                if (companyMaster != null) {
                    mCompanyMaster fitnew = new mCompanyMaster();
                    fitnew.setCmpid(companyMaster.getCmpid());
                    fitnew.setCmpcode(companyMaster.getCmpcode());
                    fitnew.setCmpnm(companyMaster.getCmpnm());

                    checked.setCompanyMaster(fitnew);
                }
                List<mClientSPOCMaster> mClientSPOCMasters = checked.getMClientSPOCMasters();
                if (mClientSPOCMasters != null) {
                    List<mClientSPOCMaster> firnewspoc = new ArrayList<>();
                    for (mClientSPOCMaster checkspoc : mClientSPOCMasters) {
                        checkspoc.setClientmasterId(null);
                        firnewspoc.add(checkspoc);
                    }
                    checked.setMClientSPOCMasters(firnewspoc);
                }
                List<mClientCMEMaster> mClientCMEMasterList = checked.getMClientCMEMasterList();
                if (mClientCMEMasterList != null) {
                    List<mClientCMEMaster> fitnewcme = new ArrayList<>();
                    for (mClientCMEMaster checkedcme : mClientCMEMasterList) {

                        checkedcme.setClientMasterIdCme(null);
                        mModulesMaster cmemodulesMaster = checkedcme.getCmemodulesMaster();
                        if (cmemodulesMaster != null) {
                            mModulesMaster fitnewmod = new mModulesMaster();
                            fitnewmod.setModId(cmemodulesMaster.getModId());
                            fitnewmod.setModcode(cmemodulesMaster.getModcode());
                            fitnewmod.setIsactive(cmemodulesMaster.getIsactive());
                            fitnewmod.setCompanyMaster(null);
                            fitnewmod.setEmployeeMasterList(null);
                            fitnewmod.setMClientCMEMasterList(null);
                            checkedcme.setCmemodulesMaster(fitnewmod);
                        }
//                        checkedcme.setCmemodulesMaster(cmemodulesMaster);
                        if(checkedcme.getIsActive()){
                            fitnewcme.add(checkedcme);
                        }


                    }
                   if(!fitnewcme.isEmpty() && fitnewcme!=null){
                       checked.setMClientCMEMasterList(fitnewcme);
                   }else {
                       checked.setMClientCMEMasterList(null);
                   }
                }
                    return checked;

            }else{
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public mClientMaster getClientPresentInActiveCMEByID(Integer id) {
        try {
            mClientMaster checked = clientRepository.findById(id).orElse(null);
            if (checked != null) {
                mCompanyMaster companyMaster = checked.getCompanyMaster();
                if (companyMaster != null) {
                    mCompanyMaster fitnew = new mCompanyMaster();
                    fitnew.setCmpid(companyMaster.getCmpid());
                    fitnew.setCmpcode(companyMaster.getCmpcode());
                    fitnew.setCmpnm(companyMaster.getCmpnm());

                    checked.setCompanyMaster(fitnew);
                }
                List<mClientSPOCMaster> mClientSPOCMasters = checked.getMClientSPOCMasters();
                if (mClientSPOCMasters != null) {
                    List<mClientSPOCMaster> firnewspoc = new ArrayList<>();
                    for (mClientSPOCMaster checkspoc : mClientSPOCMasters) {
                        checkspoc.setClientmasterId(null);
                        firnewspoc.add(checkspoc);
                    }
                    checked.setMClientSPOCMasters(firnewspoc);
                }
                List<mClientCMEMaster> mClientCMEMasterList = checked.getMClientCMEMasterList();
                if (mClientCMEMasterList != null) {
                    List<mClientCMEMaster> fitnewcme = new ArrayList<>();
                    for (mClientCMEMaster checkedcme : mClientCMEMasterList) {

                        checkedcme.setClientMasterIdCme(null);
                        mModulesMaster cmemodulesMaster = checkedcme.getCmemodulesMaster();
                        if (cmemodulesMaster != null) {
                            mModulesMaster fitnewmod = new mModulesMaster();
                            fitnewmod.setModId(cmemodulesMaster.getModId());
                            fitnewmod.setModcode(cmemodulesMaster.getModcode());
                            fitnewmod.setIsactive(cmemodulesMaster.getIsactive());
                            fitnewmod.setCompanyMaster(null);
                            fitnewmod.setEmployeeMasterList(null);
                            fitnewmod.setMClientCMEMasterList(null);
                            checkedcme.setCmemodulesMaster(fitnewmod);
                        }
//                        checkedcme.setCmemodulesMaster(cmemodulesMaster);
                        if(checkedcme.getIsActive()){
                        }else{
                            fitnewcme.add(checkedcme);

                        }


                    }
                   if(!fitnewcme.isEmpty() && fitnewcme!=null){
                       checked.setMClientCMEMasterList(fitnewcme);
                   }else {
                       checked.setMClientCMEMasterList(null);
                   }
                }
                    return checked;

            }else{
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public mClientMaster getClientById(Integer id) {
        try {
            mClientMaster checked = clientRepository.findById(id).orElse(null);
            if (checked != null) {
                mCompanyMaster companyMaster = checked.getCompanyMaster();
                if (companyMaster != null) {
                    mCompanyMaster fitnew = new mCompanyMaster();
                    fitnew.setCmpid(companyMaster.getCmpid());
                    fitnew.setCmpcode(companyMaster.getCmpcode());
                    fitnew.setCmpnm(companyMaster.getCmpnm());

                    checked.setCompanyMaster(fitnew);
                }
                List<mClientSPOCMaster> mClientSPOCMasters = checked.getMClientSPOCMasters();
                if (mClientSPOCMasters != null) {
                    List<mClientSPOCMaster> firnewspoc = new ArrayList<>();
                    for (mClientSPOCMaster checkspoc : mClientSPOCMasters) {
                        checkspoc.setClientmasterId(null);
                        firnewspoc.add(checkspoc);
                    }
                    checked.setMClientSPOCMasters(firnewspoc);
                }
                List<mClientCMEMaster> mClientCMEMasterList = checked.getMClientCMEMasterList();
                if (mClientCMEMasterList != null) {
                    List<mClientCMEMaster> fitnewcme = new ArrayList<>();
                    for (mClientCMEMaster checkedcme : mClientCMEMasterList) {
                        checkedcme.setClientMasterIdCme(null);
                        mModulesMaster cmemodulesMaster = checkedcme.getCmemodulesMaster();
                        if (cmemodulesMaster != null) {
                            mModulesMaster fitnewmod = new mModulesMaster();
                            fitnewmod.setModId(cmemodulesMaster.getModId());
                            fitnewmod.setModcode(cmemodulesMaster.getModcode());
                            fitnewmod.setIsactive(cmemodulesMaster.getIsactive());
                            fitnewmod.setCompanyMaster(null);
                            fitnewmod.setEmployeeMasterList(null);
                            fitnewmod.setMClientCMEMasterList(null);
                            checkedcme.setCmemodulesMaster(fitnewmod);
                        }
//                        checkedcme.setCmemodulesMaster(cmemodulesMaster);
                        fitnewcme.add(checkedcme);


                    }
                    checked.setMClientCMEMasterList(fitnewcme);



                }
                return checked;


            }else{
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public mClientMaster getClientByIDforcme(Integer id) {
        try {
            mClientMaster checked = clientRepository.findById(id).orElse(null);
            if (checked != null) {
                mCompanyMaster companyMaster = checked.getCompanyMaster();
                if (companyMaster != null) {
                    mCompanyMaster fitnew = new mCompanyMaster();
                    fitnew.setCmpid(companyMaster.getCmpid());
                    fitnew.setCmpcode(companyMaster.getCmpcode());
                    fitnew.setCmpnm(companyMaster.getCmpnm());
                    checked.setCompanyMaster(fitnew);
                }
                checked.setMClientSPOCMasters(null);
                List<mClientCMEMaster> mClientCMEMasterList = checked.getMClientCMEMasterList();
                if (mClientCMEMasterList != null) {
                    List<mClientCMEMaster> fitnewcme = new ArrayList<>();
                    for (mClientCMEMaster checkedcme : mClientCMEMasterList) {
                        checkedcme.setClientMasterIdCme(null);
                        mModulesMaster cmemodulesMaster = checkedcme.getCmemodulesMaster();
                        if (cmemodulesMaster != null) {
                            mModulesMaster fitnewmod = new mModulesMaster();
                            fitnewmod.setModId(cmemodulesMaster.getModId());
                            fitnewmod.setModcode(cmemodulesMaster.getModcode());
                            fitnewmod.setIsactive(cmemodulesMaster.getIsactive());
                            fitnewmod.setCompanyMaster(null);
                            fitnewmod.setEmployeeMasterList(null);
                            fitnewmod.setMClientCMEMasterList(null);
                            checkedcme.setCmemodulesMaster(fitnewmod);
                        }
//                        checkedcme.setCmemodulesMaster(cmemodulesMaster);
                        fitnewcme.add(checkedcme);


                    }
                    checked.setMClientCMEMasterList(fitnewcme);



                }
                return checked;


            }else{
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public mClientMaster getClientByCode(String Code) {
       try{
           mClientMaster checked = clientRepository.findByclientCode(Code).orElse(null);
           if (checked != null) {
               mCompanyMaster companyMaster = checked.getCompanyMaster();
               if (companyMaster != null) {
                   mCompanyMaster fitnew = new mCompanyMaster();
                   fitnew.setCmpid(companyMaster.getCmpid());
                   fitnew.setCmpcode(companyMaster.getCmpcode());
                   fitnew.setCmpnm(companyMaster.getCmpnm());

                   checked.setCompanyMaster(fitnew);
               }
               List<mClientSPOCMaster> mClientSPOCMasters = checked.getMClientSPOCMasters();
               if (mClientSPOCMasters != null) {
                   List<mClientSPOCMaster> firnewspoc = new ArrayList<>();
                   for (mClientSPOCMaster checkspoc : mClientSPOCMasters) {
                       checkspoc.setClientmasterId(null);
                       firnewspoc.add(checkspoc);
                   }
                   checked.setMClientSPOCMasters(firnewspoc);
               }
               List<mClientCMEMaster> mClientCMEMasterList = checked.getMClientCMEMasterList();
               if (mClientCMEMasterList != null) {
                   List<mClientCMEMaster> fitnewcme = new ArrayList<>();
                   for (mClientCMEMaster checkedcme : mClientCMEMasterList) {
                       checkedcme.setClientMasterIdCme(null);
                       mModulesMaster cmemodulesMaster = checkedcme.getCmemodulesMaster();
                       if (cmemodulesMaster != null) {
                           mModulesMaster fitnewmod = new mModulesMaster();
                           fitnewmod.setModId(cmemodulesMaster.getModId());
                           fitnewmod.setModcode(cmemodulesMaster.getModcode());
                           fitnewmod.setIsactive(cmemodulesMaster.getIsactive());
                           fitnewmod.setCompanyMaster(null);
                           fitnewmod.setEmployeeMasterList(null);
                           fitnewmod.setMClientCMEMasterList(null);
                           checkedcme.setCmemodulesMaster(fitnewmod);

                       }
                       fitnewcme.add(checkedcme);
                   }
                   checked.setMClientCMEMasterList(fitnewcme);
               }

               return checked;


           }else{
               return null;
           }
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
    }

    public Optional<mClientMaster> getClientByname(String name) {
       try{
           return clientRepository.findByclientName(name);

       } catch (Exception e) {
           throw new RuntimeException(e);
       }
    }

    public mClientMaster updateClient(Integer id, mClientMaster updatedClient) {
        try{
            mClientMaster mClientMasterOld = clientRepository.findById(id).orElse(null);
            if(mClientMasterOld!=null){
//                mClientMasterOld.setClientCode(updatedClient.getClientCode());
                mClientMasterOld.setClientName(updatedClient.getClientName());
                mClientMasterOld.setClientGroup(updatedClient.getClientGroup());
                mClientMasterOld.setClientModule(updatedClient.getClientModule());
                mClientMasterOld.setIsactive(updatedClient.getIsactive());
                mClientMasterOld.setCompanyMaster(updatedClient.getCompanyMaster());
                mClientMaster save = clientRepository.save(mClientMasterOld);
                return save;

            }else{
                return null;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public mClientMaster updateByclientCode(String clientCode, mClientMaster updatedClient) {
        try{
            mClientMaster mClientMasterOld = clientRepository.findByclientCode(clientCode).orElse(null);
            if(mClientMasterOld!=null){
//                mClientMasterOld.setClientCode(updatedClient.getClientCode());
                mClientMasterOld.setClientName(updatedClient.getClientName());
                mClientMasterOld.setClientGroup(updatedClient.getClientGroup());
                mClientMasterOld.setClientModule(updatedClient.getClientModule());
                mClientMasterOld.setIsactive(updatedClient.getIsactive());
                mClientMasterOld.setCompanyMaster(updatedClient.getCompanyMaster());
                mClientMaster save = clientRepository.save(mClientMasterOld);
                return save;

            }else{
                return null;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteClient(Integer id) {
       try{
           mClientMaster mClientMaster = getClientById(id);
           if(mClientMaster!=null){
               clientRepository.deleteById(id);
               return true;
           }else{
               return false;
           }

       } catch (Exception e) {
           throw new RuntimeException(e);
       }
    }

    @Transactional
    public mClientMaster updateClientIsActive(Integer clientId, Boolean isActive) {
        try{
        mClientMaster client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found with ID: " + clientId));

        client.setIsactive(isActive);
        return clientRepository.save(client);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}

