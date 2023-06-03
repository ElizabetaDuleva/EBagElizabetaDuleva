package bg.ebag;

import base.TestUtil;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

public class SuccessfulLogin extends TestUtil {

    @Test(dataProvider = "correctUser")

    public void successfulLogin(String email, String password) {

        WebElement profileBtn = driver.findElement(By.className("icon-inline s-profile"));
        profileBtn.click();
        profileBtn.isDisplayed();

        FluentWait fluentWait = new FluentWait(driver)
                .withTimeout(Duration.ofSeconds(3));
        fluentWait.until(ExpectedConditions.elementToBeClickable(profileBtn));
        Assert.assertTrue(profileBtn.isDisplayed());

        WebElement emailInput = driver.findElement(By.id(email));
        emailInput.click();
        emailInput.clear();
        emailInput.sendKeys(email);

        WebElement passwordInput = driver.findElement(By.id(password));
        passwordInput.click();
        passwordInput.clear();
        emailInput.sendKeys(password);

        WebElement loginBtn = driver.findElement(By.className("btn login-btn"));
        loginBtn.click();

        WebElement profile = driver.findElement(By.className("icon-inline s-profile"));
        profile.click();

        Assert.assertTrue(profile.isDisplayed());

        WebElement logoutLink = driver.findElement(By.linkText("Изход"));
        logoutLink.click();
    }

    @DataProvider(name = "wrongUser")

    public Object[][] getUser() {
        return new Object[][]{
                {"wrongEmail", "KkJj345!"},
                {"bettyhikova@abv.bg", "wrongPassword"},
                {"wrong", "wrong"}

        };
    }

    @Test(dataProvider = "wrongUser")
    public void unsuccessfulLogin(String email, String password) {
        WebElement emailInput = driver.findElement(By.id(email));
        emailInput.click();
        emailInput.clear();
        emailInput.sendKeys(email);

        WebElement passwordInput = driver.findElement(By.id(password));
        passwordInput.click();
        passwordInput.clear();
        emailInput.sendKeys(password);

        WebElement loginBtn = driver.findElement(By.className("btn login-btn"));
        loginBtn.click();

        WebElement errorMessage = driver.findElement(By.className("global-errors-container"));
        Assert.assertTrue(errorMessage.isDisplayed());
    }

    @DataProvider(name = "correctUser")
    public Object[][] readUserFromCsv() {
        try {
            CSVReader csvReader = new CSVReader(new FileReader("src/test/resources/correctUser.csv"));
            List<String[]> csvData = csvReader.readAll();
            Object[][] csvDataObj = new Object[csvData.size()][2];

            for (int i = 0; i < csvData.size(); i++) {
                csvDataObj[i] = csvData.get(i);
            }
            return csvDataObj;

        } catch (IOException e) {
            System.out.println("Not possible to find the file!");
            return null;
        } catch (CsvException e) {
            return null;
        }
    }
}
