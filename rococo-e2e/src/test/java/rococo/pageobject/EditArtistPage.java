package rococo.pageobject;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import rococo.model.ArtistJson;
import rococo.model.PaintingJson;

import java.io.File;

import static com.codeborne.selenide.Selenide.$;

public class EditArtistPage extends BasePage<EditArtistPage> {

    private final SelenideElement photoInput = $("form input[type='file']");
    private final SelenideElement nameInput = $("input[name='name']");
    private final SelenideElement biographyTextarea = $("textarea[name='biography']");
    private final SelenideElement cancelButton = $("div.text-right button[type='button']");
    private final SelenideElement saveButton = $("button[type='submit']");

    @Step("Заполнить карточку художника: имя '{artist.name()}', биография '{artist.biography()}'")
    public EditArtistPage addArtist(ArtistJson artist) {
        nameInput.sendKeys(artist.name());
        photoInput.sendKeys(new File(artist.photo()).getAbsolutePath());
        biographyTextarea.sendKeys(artist.biography());
        return this;
    }

    @Step("Изменить имя художника на '{namePainting}'")
    public EditArtistPage editNameArtist(String namePainting) {
        nameInput.clear();
        nameInput.sendKeys(namePainting);
        return this;
    }

    @Step("Заменить фото художника (файл: '{urlFile}')")
    public EditArtistPage editPhotoArtist(String urlFile) {
        photoInput.sendKeys(new File(urlFile).getAbsolutePath());
        return this;
    }

    @Step("Изменить биографию художника на '{biographyArtist}'")
    public EditArtistPage editBiographyArtist(String biographyArtist) {
        biographyTextarea.clear();
        biographyTextarea.sendKeys(biographyArtist);
        return this;
    }

    @Step("Сохранить нового художника")
    public ArtistPage saveNewData() {
        saveButton.click();
        return new ArtistPage();
    }

    @Step("Сохранить изменения в карточке художника")
    public ArtistCardPage saveUpdateData() {
        saveButton.click();
        return new ArtistCardPage();
    }

    @Step("Нажать кнопку 'Сохранить' (без перехода)")
    public EditArtistPage clickSave() {
        saveButton.click();
        return this;
    }

    @Step("Отменить редактирование карточки художника")
    public ArtistPage cancelSaveData() {
        cancelButton.click();
        return new ArtistPage();
    }
}
