package com.pikabu.testing.functional;

import com.pikabu.testing.base.BaseTest;
import com.pikabu.testing.pages.LoginForm;
import com.pikabu.testing.pages.LoginPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginFunctionalityTest extends BaseTest {

    private LoginPage loginPage;
    private LoginForm form;

    @BeforeMethod(alwaysRun = true)
    public void openLogin() {
        loginPage = new LoginPage(driver);
        loginPage.open();
        form = new LoginForm(driver);
        assertThat(form.isVisible())
                .as("Ожидалась видимая форма входа")
                .isTrue();
    }

    @Test(description = "Форма логина отображается и готова к вводу")
    public void testLoginFormVisible() {
        assertThat(form.isVisible()).isTrue();
    }

    @Test(description = "Неверные креды → показывается ошибка")
    public void testInvalidCredentialsShowsError() {
        loginPage.open();
        assertThat(loginPage.isOpened()).as("Страница логина должна открыться").isTrue();

        var form = loginPage.getForm();
        form.typeUsername("wrong_user")
                .typePassword("wrong_pass");

        boolean hasErr = form.submitAndWaitForError(10);
        assertThat(hasErr)
                .as("Должно показываться сообщение об ошибке при неправильных данных. Получено: '%s'", form.errorText())
                .isTrue();
    }

    @Test(description = "Можно ввести данные и сабмитнуть форму (smoke)")
    public void testCanTypeAndSubmit() {
        form.typeUsername("some_user")
                .typePassword("some_password_1");
        form.submit();

        assertThat(form.isVisible() || form.hasError())
                .as("После сабмита страница/форма должна оставаться в рабочем состоянии")
                .isTrue();
    }
}
