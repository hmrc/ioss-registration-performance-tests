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
      postPreviousOssSchemeNumber(2, 1, "SI11223344"),
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
//      Awaiting further implementation
      getBusinessContactDetails,
      postBusinessContactDetails,
      getBankDetails,
      postBankDetails
    )

  runSimulation()
}
