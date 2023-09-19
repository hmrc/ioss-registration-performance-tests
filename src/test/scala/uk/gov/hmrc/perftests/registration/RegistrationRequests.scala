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

import io.gatling.core.Predef._
import io.gatling.core.session.Expression
import io.gatling.http.Predef._
import uk.gov.hmrc.performance.conf.ServicesConfiguration

object RegistrationRequests extends ServicesConfiguration {

  val baseUrl: String = baseUrlFor("ioss-registration-frontend")
  val route: String   = "/pay-vat-on-goods-sold-to-eu/register-for-import-one-stop-shop"

  val loginUrl = baseUrlFor("auth-login-stub")

  def inputSelectorByName(name: String): Expression[String] = s"input[name='$name']"

  def getAlreadyRegisteredForIOSS =
    http("Get Already Registered for IOSS page")
      .get(s"$baseUrl$route/ioss-registered")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postAlreadyRegisteredForIOSS =
    http("Post Already Registered for IOSS page")
      .post(s"$baseUrl$route/ioss-registered")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value", false)
      .check(status.in(303))
      .check(header("Location").is(s"$route/selling-goods-outside-single-market"))

  def getSellingGoodsOutsideSingleMarket =
    http("Get Selling Goods Outside Single Market page")
      .get(s"$baseUrl$route/selling-goods-outside-single-market")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postSellingGoodsOutsideSingleMarket =
    http("Post Selling Goods Outside Single Market page")
      .post(s"$baseUrl$route/selling-goods-outside-single-market")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value", true)
      .check(status.in(303))
      .check(header("Location").is(s"$route/goods-value"))

  def getGoodsValue =
    http("Get Goods Value page")
      .get(s"$baseUrl$route/goods-value")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postGoodsValue =
    http("Post Goods Value page")
      .post(s"$baseUrl$route/goods-value")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value", true)
      .check(status.in(303))
      .check(header("Location").is(s"$route/registered-for-vat-in-uk"))

  def getRegisteredForUKVAT =
    http("Get Registered for VAT in UK page")
      .get(s"$baseUrl$route/registered-for-vat-in-uk")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postRegisteredForUKVAT =
    http("Post Registered for VAT in UK page")
      .post(s"$baseUrl$route/registered-for-vat-in-uk")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value", true)
      .check(status.in(303))
      .check(header("Location").is(s"$route/ni-based"))

  def getNIBusiness =
    http("Get Northern Ireland Business page")
      .get(s"$baseUrl$route/ni-based")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postNIBusiness =
    http("Post Northern Ireland Business page")
      .post(s"$baseUrl$route/ni-based")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value", true)
      .check(status.in(303))
      .check(header("Location").is(s"$route/register-to-use-service"))

  def getRegisterToUseService =
    http("Get Register to Use Service page")
      .get(s"$baseUrl$route/register-to-use-service")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postRegisterToUseService =
    http("Post Register to Use Service page")
      .post(s"$baseUrl$route/register-to-use-service")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.in(303))
      .check(header("Location").is(s"$route/on-sign-in"))

  def getAuthorityWizard =
    http("Get Authority Wizard page")
      .get(loginUrl + s"/auth-login-stub/gg-sign-in")
      .check(status.in(200, 303))

  def postAuthorityWizard =
    http("Enter Auth login credentials ")
      .post(loginUrl + s"/auth-login-stub/gg-sign-in")
      .formParam("authorityId", "")
      .formParam("gatewayToken", "")
      .formParam("credentialStrength", "strong")
      .formParam("confidenceLevel", "50")
      .formParam("affinityGroup", "Organisation")
      .formParam("email", "user@test.com")
      .formParam("credentialRole", "User")
      .formParam("redirectionUrl", baseUrl + route)
      .formParam("enrolment[0].name", "HMRC-MTD-VAT")
      .formParam("enrolment[0].taxIdentifier[0].name", "VRN")
      .formParam("enrolment[0].taxIdentifier[0].value", "${vrn}")
      .formParam("enrolment[0].state", "Activated")
      .check(status.in(200, 303))
      .check(headerRegex("Set-Cookie", """mdtp=(.*)""").saveAs("mdtpCookie"))

  def resumeJourney =
    http("Resume journey")
      .get(s"$baseUrl$route/on-sign-in")
      .check(status.in(303))

  def getConfirmVatDetails =
    http("Get Confirm VAT Details page")
      .get(s"$baseUrl$route/confirm-vat-details")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postConfirmVatDetails =
    http("Confirm VAT Details")
      .post(s"$baseUrl$route/confirm-vat-details")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value", "yes")
      .check(status.in(200, 303))
      .check(header("Location").is(s"$route/have-uk-trading-name"))

  def getHasTradingName =
    http("Get Has Trading Name page")
      .get(s"$baseUrl$route/have-uk-trading-name")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postHasTradingName =
    http("Post Has Trading Name")
      .post(s"$baseUrl$route/have-uk-trading-name")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value", "true")
      .check(status.in(200, 303))
      .check(header("Location").is(s"$route/uk-trading-name/1"))

  def getTradingName(index: Int) =
    http("Get Trading Name page")
      .get(s"$baseUrl$route/uk-trading-name/$index")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postTradingName(index: Int, tradingName: String) =
    http("Enter Trading Name")
      .post(s"$baseUrl$route/uk-trading-name/$index")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value", tradingName)
      .check(status.in(200, 303))
      .check(header("Location").is(s"$route/add-uk-trading-name"))

  def getAddTradingName =
    http("Get Add Trading Name page")
      .get(s"$baseUrl$route/add-uk-trading-name")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def testAddTradingName(answer: Boolean) =
    http("Add Trading Name")
      .post(s"$baseUrl$route/add-uk-trading-name")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value", answer)
      .check(status.in(200, 303))

  def postAddTradingName(answer: Boolean, index: Option[Int]) =
    if (answer) {
      testAddTradingName(answer)
        .check(header("Location").is(s"$route/uk-trading-name/${index.get}"))
    } else {
      testAddTradingName(answer)
      //next section of journey not implemented yet
    }

}
