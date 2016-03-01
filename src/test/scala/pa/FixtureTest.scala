package pa

import org.scalatest.{OptionValues, FunSuite, ShouldMatchers}
import concurrent.Await
import concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global


class FixtureTest extends FunSuite with ShouldMatchers with OptionValues {

  val stubClient = StubClient
  val matchFixtureOne = Await.result(stubClient.fixtures("789"), 1.second)(0)
  val matchFixtureTwo = Await.result(stubClient.fixtures("789"), 1.second)(1)

  test("Test parser returns two fixtures for test xml") {
    val fixtures = Await.result(stubClient.fixtures("789"), 1.second)
    fixtures.length should be (2)
  }

  test("Test formation of MatchDay fixture ids") {
    matchFixtureOne.id should be ("3407177")
    matchFixtureTwo.id should be ("3407178")
  }

  test("Test MatchDay fixture dates") {
    matchFixtureOne.date.dayOfMonth.get should be (13)
    matchFixtureOne.date.monthOfYear.get should be (8)
    matchFixtureOne.date.year.get should be (2011)

    matchFixtureTwo.date.dayOfMonth.get should be (13)
    matchFixtureTwo.date.monthOfYear.get should be (8)
    matchFixtureTwo.date.year.get should be (2011)
  }

  test("Test MatchDay fixture round") {
    matchFixtureOne.round.roundNumber should be ("1")
    matchFixtureOne.round.name.value should be ("League")

    matchFixtureTwo.round.roundNumber should be ("7")
    matchFixtureTwo.round.name.value should be ("round")
  }

  test("Test MatchDay fixture legs") {
    matchFixtureOne.leg should be ("1")
    matchFixtureTwo.leg should be ("7")
  }

  test("Test home and away teams within MatchDay fixture") {
    matchFixtureOne.homeTeam.id should be ("22")
    matchFixtureOne.homeTeam.name should be ("Blackburn")
    matchFixtureOne.awayTeam.id should be ("44")
    matchFixtureOne.awayTeam.name should be ("Wolverhampton")

    matchFixtureTwo.homeTeam.id should be ("55")
    matchFixtureTwo.homeTeam.name should be ("Fulham")
    matchFixtureTwo.awayTeam.id should be ("2")
    matchFixtureTwo.awayTeam.name should be ("Aston Villa")
  }

  test("Test MatchDay fixture venue") {
    matchFixtureOne.venue.map(_.id).getOrElse("") should be ("59")
    matchFixtureOne.venue.map(_.name).getOrElse("") should be ("Ewood Park")

    matchFixtureTwo.venue.map(_.id).getOrElse("") should be ("60")
    matchFixtureTwo.venue.map(_.name).getOrElse("") should be ("Craven Cottage")
  }

  test("Test MatchDay fixture stage") {
    matchFixtureOne.stage.stageNumber should be ("1")
    matchFixtureTwo.stage.stageNumber should be ("1")
  }
  
  val fixtures = Await.result(stubClient.fixtures, 1.second)

  test("Test can get all Fixtures across all competitions") {
    fixtures(0).id should be ("3407177")
    fixtures(1).id should be ("3407178")
  }
  
  test("Test Fixture with a competition") {
    fixtures(0).competition.map(_.id).getOrElse("") should be ("100")
    fixtures(0).competition.map(_.name).getOrElse("") should be ("Barclays Premier League 11/12")
  }
  
  test("Test Fixture without a competition") {
    fixtures(1).competition should be (None)
  }

  test("Test fixture throw exception if it encounters errors") {
    try {
      Await.result(stubClient.fixtures("errors"), 1.second)
      assert(false, "Exception should have been thrown")
    }
    catch {
      case e: PaClientErrorsException => assert(true)
    }
  }
  
  
}
