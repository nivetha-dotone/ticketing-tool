package com.dot1.ticket_track.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
@Controller
@RequestMapping("/track")
public class StarterPage {

    @GetMapping("/")
    public String LoginPage(){
        return  "Login.html";
    }

    @GetMapping("/adminDashboard")
    public String DashBoardAdminPage(){
        return  "Admin/Dashboard_OF_Admin.html";
    }

    @GetMapping("/adminmaster")
    public String mastersAdminPage(){
        return  "Admin/Masters.html";
    }
    @GetMapping("/adminticketCr")
    public String ticketcreationAdminPage(){
        return  "Admin/ticketcreation.html";
    }
    @GetMapping("/admintAlltickets")
    public String alltktAdminPage(){
        return  "Admin/All_tickets_admin.html";
    }
    @GetMapping("/admintReports")
    public String reportsAdminPage(){
        return  "Admin/reports_selection.html";
    }
    @GetMapping("/Logout")
    public String LogoutPage(){
        return  "Login.html";
    }
    @GetMapping("/acompany")
    public String cmpAdminPage(){
        return  "Admin/newCompanyMaster.html";
    }
    @GetMapping("/amodule")
    public String modAdminPage(){
        return  "Admin/ModuleMaster.html";
    }
    @GetMapping("/auserLogin")
    public String userAdmin(){
        return  "Admin/userlogin.html";
    }
    @GetMapping("/aclient")
    public String clinetAdminPage(){
        return  "Admin/clientMasters.html";
    }
    @GetMapping("/acmexp")
    public String cmeAdminPage(){
        return  "Admin/cmeDashboardMaster.html";
    }
    @GetMapping("/ademp")
    public String empAdminPage(){
        return  "Admin/employeeMaster.html";
    }
    @GetMapping("/admrole")
    public String roleAdminPage(){
        return  "Admin/roleMaster.html";
    }
    @GetMapping("/adgmMaster")
    public String gmAdminPage(){
        return  "Admin/new_general_master.html";
    }
//    @GetMapping("/admin/gmMaster")
//    public String gmAdminPage(){
//        return  "Admin/new_general_master.html";
//    }

//    @GetMapping("/adminDashboard")
//    public String DashBoardAdminPage1(){
//        return  "Admin/Dashboard_OF_Admin.html";
//    }

//    @GetMapping("/admin/adminmaster")
//    public String mastersAdminPage2(){
//        return  "Admin/Masters.html";
//    }
//    @GetMapping("/admin/adminticketCr")
//    public String ticketcreationAdminPage3(){
//        return  "Admin/ticketcreation.html";
//    }
//    @GetMapping("/admin/admintAlltickets")
//    public String alltktAdminPage4(){
//        return  "Admin/All_tickets_admin.html";
//    }
//    @GetMapping("/admin/admintReports")
//    public String reportsAdminPage5(){
//        return  "Admin/reports_selection.html";
//    }
//
//    @GetMapping("/admin/Logout")
//    public String adminLogoutPage(){
//        return  "Login.html";
//    }









    //Client
    @GetMapping("/clientDashboard")
    public String ClientDashboardPage(){
        return  "Client/client-dashboard.html";
    }

    @GetMapping("/clienttktALL")
    public String ClinetalltktPage(){
        return  "Client/View-All-tickets-client.html";
    }
    @GetMapping("/clientcreation")
    public String clinetcreaPage(){
        return  "Client/TicketCreation.html";
    }






    //Consultant
    @GetMapping("/constdashboard")
    public String ConstDashboardPage(){
        return  "Employee/Employe.html";
    }

     @GetMapping("/consttkdwindow")
    public String ConstDashboardPage1(){
        return  "Employee/TicketsWindow.html";
    }
  @GetMapping("/constmyticketpage")
    public String ConstDashboardPage2(){
        return  "Employee/consultant-my-tickets.html";
    }






    //Manager
    @GetMapping("/managerDashboard")
    public String managerDashboardPage(){
        return  "Manager/manager-dashboard.html";
    }


     @GetMapping("/managerAllt")
    public String managerDashboardPage1(){
        return  "Manager/View-All-tickets-manager.html";
    }
    @GetMapping("/managermytikct")
    public String managerDashboardPage2(){
        return  "Manager/manager-my-tickets.html";
    }
    @GetMapping("/managertktCreation")
    public String managerDashboardPage3(){
        return  "Manager/TicketCreation.html";
    }




}
