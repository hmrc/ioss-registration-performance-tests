/*
 * Copyright 2023 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.perftests.registration

import uk.gov.hmrc.performance.simulation.PerformanceTestRunner
import uk.gov.hmrc.perftests.registration.RegistrationRequests._

class RegistrationSimulation extends PerformanceTestRunner {

  setup("registration", "Registration Journey") withRequests
    (
      getAuthorityWizard,
      postAuthorityWizard,
      getAlreadyRegisteredForIOSS,
      postAlreadyRegisteredForIOSS,
      getSellingGoodsOutsideSingleMarket,
      postSellingGoodsOutsideSingleMarket,
      getGoodsValue,
      postGoodsValue,
      getRegisteredForUKVAT,
      postRegisteredForUKVAT,
      getNIBusiness,
      postNIBusiness,
      getRegisterToUseService,
      postRegisterToUseService,
      resumeJourney,
      getConfirmVatDetails,
      postConfirmVatDetails,
      getHasTradingName,
      postHasTradingName,
      getTradingName(1),
      postTradingName(1, "First trading name"),
      getAddTradingName,
      postAddTradingName(true, Some(2)),
      getTradingName(2),
      postTradingName(2, "2nd trading name"),
      getAddTradingName,
      postAddTradingName(false, None),
      getPreviousOss,
      postPreviousOss(1),
      getPreviousCountry(1),
      postPreviousCountry(1, 1, "CY"),
      getPreviousScheme(1, 1),
      postPreviousScheme(1, 1, "oss"),
      getPreviousOssSchemeNumber(1, 1),
      postPreviousOssSchemeNumber(1, 1, "CY11145678X"),
      getPreviousSchemeAnswers(1),
      postPreviousSchemeAnswers(1, false),
      getPreviousSchemesOverview,
      postPreviousSchemesOverview(true, Some(2)),
      getPreviousCountry(2),
      postPreviousCountry(2, 1, "SI"),
      getPreviousScheme(2, 1),
      postPreviousScheme(2, 1, "oss"),
      getPreviousOssSchemeNumber(2, 1),
      postPreviousOssSchemeNumber(2, 1, "SI44332211"),
      getPreviousSchemeAnswers(2),
      postPreviousSchemeAnswers(2, false),
      getPreviousSchemesOverview,
      postPreviousSchemesOverview(true, Some(3)),
      getPreviousCountry(3),
      postPreviousCountry(3, 1, "MT"),
      getPreviousScheme(3, 1),
      postPreviousScheme(3, 1, "ioss"),
      getPreviousIossScheme(3, 1),
      postPreviousIossScheme(3, 1, true),
      getPreviousIossNumber(3, 1),
      postPreviousIossNumber(3, 1, "IM4707744112", "IN4706852130"),
      getPreviousSchemeAnswers(3),
      postPreviousSchemeAnswers(3, false),
      getPreviousSchemesOverview,
      postPreviousSchemesOverview(false, None),
      getIsTaxRegisteredInEu,
      postIsTaxRegisteredInEu(1),
      getVatRegisteredInEuMemberState(1),
      postVatRegisteredInEuMemberState(1, "AT"),
      getHowDoYouOperate(1),
      postHowDoYouOperate(1),
      getRegistrationType(1),
      postRegistrationType(1, "vatNumber"),
      getEuVatNumber(1),
      postEuVatNumber(1, "ATU88882211"),
      getFixedEuTradingName(1),
      postFixedEuTradingName(1, "Austrian Goods"),
      getFixedEstablishmentAddress(1),
      postFixedEstablishmentAddress(1),
      getCheckTaxDetails(1),
      postCheckTaxDetails(1),
      getAddTaxDetails,
      postAddTaxDetails(true, Some(2)),
      getVatRegisteredInEuMemberState(2),
      postVatRegisteredInEuMemberState(2, "NL"),
      getHowDoYouOperate(2),
      postHowDoYouOperate(2),
      getRegistrationType(2),
      postRegistrationType(2, "taxId"),
      getEuTaxReference(2),
      postEuTaxReference(2, "NL12345678ABC"),
      getFixedEuTradingName(2),
      postFixedEuTradingName(2, "Netherlands Trading"),
      getFixedEstablishmentAddress(2),
      postFixedEstablishmentAddress(2),
      getCheckTaxDetails(2),
      postCheckTaxDetails(2),
      getAddTaxDetails,
      postAddTaxDetails(false, Some(1)),
      getWebsite(1),
      postWebsite(1, "www.websiteone.com"),
      getAddWebsite,
      postAddWebsite(true, Some(2)),
      getWebsite(2),
      postWebsite(2, "www.anotherwebsite.com"),
      getAddWebsite,
      postAddWebsite(false, None),
      getBusinessContactDetails,
      postBusinessContactDetails,
      getBankDetails,
      postBankDetails,
      getCheckYourAnswers,
      postCheckYourAnswers,
      getRegistrationSuccessful
    )

  runSimulation()
}
