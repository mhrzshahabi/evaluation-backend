//package com.nicico.evaluation.equipment.standard_test_status;
//
//
//import lombok.AllArgsConstructor;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.context.MessageSource;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//import java.util.Locale;
//import java.util.Optional;
//@AllArgsConstructor
//@Component
//public class StandardTestStatusSeeder implements ApplicationRunner {
//   StandardTestStatusRepository standardTestRepository;
//    MessageSource messageSource;
//
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        StandardTestStatus iranianStandardOrganizationAcceptedStandardTest=new StandardTestStatus();
//        String  iranianStandardOrganizationAcceptedStandardTestTitle=    messageSource.getMessage("iranina_standarad_organization_accepted_title",null, Locale.ENGLISH);
//        String iranianStandardOrganizationAcceptedStandardTestDescription=    messageSource.getMessage("iranina_standarad_organization_accepted_decription",null, Locale.ENGLISH);
//        iranianStandardOrganizationAcceptedStandardTest.setTitle(iranianStandardOrganizationAcceptedStandardTestTitle);
//        iranianStandardOrganizationAcceptedStandardTest.setDescription(iranianStandardOrganizationAcceptedStandardTestDescription);
//        iranianStandardOrganizationAcceptedStandardTest.setTitleColor("#72cfc5");
//        iranianStandardOrganizationAcceptedStandardTest.setUpdatable(false);
//
//        Optional<StandardTestStatus>  lastIranianStandardOrganizationAcceptedStandardTest=standardTestRepository.findByTitle(iranianStandardOrganizationAcceptedStandardTestTitle);
//         if (!lastIranianStandardOrganizationAcceptedStandardTest.isPresent()){
//             standardTestRepository.save(iranianStandardOrganizationAcceptedStandardTest);
//          }
//
//         /////////////////////////////////////////////////
//
//        StandardTestStatus acceptedAndTestedStandardTest=new StandardTestStatus();
//        String  acceptedAndtestedStandardTestTitle=    messageSource.getMessage("accepted_and_tested_title",null, Locale.ENGLISH);
//        String  acceptedAndtestedStandardTestDescription=    messageSource.getMessage("accepted_and_tested_description",null, Locale.ENGLISH);
//        acceptedAndTestedStandardTest.setTitle(acceptedAndtestedStandardTestTitle);
//        acceptedAndTestedStandardTest.setDescription(acceptedAndtestedStandardTestDescription);
//        acceptedAndTestedStandardTest.setTitleColor("#72cfc5");
//        acceptedAndTestedStandardTest.setUpdatable(false);
//        Optional<StandardTestStatus>  lastStandardTestAcceptedAndTestedStandardTest=standardTestRepository.findByTitle(acceptedAndtestedStandardTestTitle);
//        if (!lastStandardTestAcceptedAndTestedStandardTest.isPresent()){
//            standardTestRepository.save(acceptedAndTestedStandardTest);
//        }
//
//        /////////////////////////////////////////////////////
//
//        StandardTestStatus acceptedStandardTest=new StandardTestStatus();
//        String  acceptedStandardTestTitle=    messageSource.getMessage("accepted_title",null, Locale.ENGLISH);
//        String  acceptedStandardTestDescription=    messageSource.getMessage("accepted_description",null, Locale.ENGLISH);
//        acceptedStandardTest.setTitle(acceptedStandardTestTitle);
//        acceptedStandardTest.setDescription(acceptedStandardTestDescription);
//        acceptedStandardTest.setTitleColor("#72cfc5");
//        acceptedStandardTest.setCreatedDate(new Date());
//        acceptedStandardTest.setCreatedBy("seeder");
//        acceptedStandardTest.setUpdatable(false);
//        Optional<StandardTestStatus>  lastAcceptedStandardTest=standardTestRepository.findByTitle(acceptedStandardTestTitle);
//        if (!lastAcceptedStandardTest.isPresent()){
//            standardTestRepository.save(acceptedStandardTest);
//        }
/////////////////////////////////////////////////////////////////////////
//
//        StandardTestStatus rejectedStandardTest=new StandardTestStatus();
//        String  rejectedStandardTestTitle=    messageSource.getMessage("rejected_title",null, Locale.ENGLISH);
//        String  rejectedStandardTestDescription=    messageSource.getMessage("rejected_description",null, Locale.ENGLISH);
//        rejectedStandardTest.setTitle(rejectedStandardTestTitle);
//        rejectedStandardTest.setDescription(rejectedStandardTestDescription);
//        rejectedStandardTest.setTitleColor("#db242c");
//
//        rejectedStandardTest.setUpdatable(false);
//        Optional<StandardTestStatus>  lastRejectedStandardTest=standardTestRepository.findByTitle(rejectedStandardTestTitle);
//        if (!lastRejectedStandardTest.isPresent()){
//            standardTestRepository.save(rejectedStandardTest);
//        }
//
//
//        ///////////////////////////////////////////////
//
//        StandardTestStatus untestedStandardTest=new StandardTestStatus();
//        String  untestedStandardTestTitle=    messageSource.getMessage("untested_title",null, Locale.ENGLISH);
//        String  untestedStandardTestDescription=    messageSource.getMessage("untested_description",null, Locale.ENGLISH);
//        untestedStandardTest.setTitle(untestedStandardTestTitle);
//        untestedStandardTest.setDescription(untestedStandardTestDescription);
//        untestedStandardTest.setTitleColor("#3d4e61");
//        untestedStandardTest.setUpdatable(false);
//        Optional<StandardTestStatus>  lastUntestedStandardTest=standardTestRepository.findByTitle(untestedStandardTestTitle);
//        if (!lastUntestedStandardTest.isPresent()){
//            standardTestRepository.save(untestedStandardTest);
//        }
//
//
//    }
//}
