package rococo.pageobject;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import rococo.model.MuseumJson;

import java.io.File;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class EditMuseumPage extends BasePage<EditMuseumPage> {

    private final SelenideElement photoInput = $("form input[type='file']");
    private final SelenideElement nameInput = $("input[name='title']");
    private final ElementsCollection countrySelect = $$("select[name='countryId'] option");
    private final SelenideElement descriptionTextarea = $("textarea[name='description']");
    private final SelenideElement cityInput = $("input[name='city'");
    private final SelenideElement cancelButton = $("div.text-right button[type='button']");
    private final SelenideElement saveButton = $("button[type='submit']");

    @Step("Заполнить все поля карточки музея: название, фото, страна, город, описание")
    public EditMuseumPage addMuseum(MuseumJson museum) {
        nameInput.sendKeys(museum.title());
        if (!museum.photo().isEmpty()) photoInput.sendKeys(new File(museum.photo()).getAbsolutePath());
        if (museum.geo().country() == null) countrySelect.get(0).click();
        cityInput.sendKeys(museum.geo().city());
        descriptionTextarea.sendKeys(museum.description());
        return this;
    }

    // Шаги редактирования полей
    @Step("Изменить название музея на '{namePainting}'")
    public EditMuseumPage editTitleMuseum(String namePainting) {
        nameInput.clear();
        nameInput.sendKeys(namePainting);
        return this;
    }

    @Step("Загрузить новое изображение музея из файла '{path}'")
    public EditMuseumPage editPhotoMuseum(String path) {
        photoInput.sendKeys(new File(path).getAbsolutePath());
        return this;
    }

    @Step("Обновить описание музея: '{description}'")
    public EditMuseumPage editDescriptionMuseum(String description) {
        descriptionTextarea.clear();
        descriptionTextarea.sendKeys(description);
        return this;
    }

    @Step("Выбрать первую страну из списка доступных")
    public EditMuseumPage editCountry() {
        countrySelect.get(0).click();
        return this;
    }

    @Step("Изменить город музея на '{city}'")
    public EditMuseumPage editCity(String city) {
        cityInput.clear();
        cityInput.sendKeys(city);
        return this;
    }

    // Шаги сохранения/отмены
    @Step("Сохранить новую карточку музея")
    public MuseumPage saveNewData() {
        saveButton.click();
        return new MuseumPage();
    }

    @Step("Сохранить изменения в карточке музея")
    public MuseumCardPage saveUpdateData() {
        saveButton.click();
        return new MuseumCardPage();
    }

    @Step("Нажать кнопку 'Сохранить' без перехода")
    public EditMuseumPage clickSave() {
        saveButton.click();
        return this;
    }

    @Step("Отменить создание/редактирование карточки музея")
    public MuseumPage cancelSaveData() {
        cancelButton.click();
        return new MuseumPage();
    }

    // Валидационные шаги
    @Step("Проверить, что кнопка 'Сохранить' отображается на странице")
    public EditMuseumPage checkButtonSaveVisible() {
        saveButton.should(visible);
        return this;
    }
}
