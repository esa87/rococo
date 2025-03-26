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

    @Step("Заполняем карточку художника")
    public EditArtistPage addArtist(ArtistJson artist) {
        nameInput.sendKeys(artist.name());
        photoInput.sendKeys(new File(artist.photo()).getAbsolutePath());
        biographyTextarea.sendKeys(artist.biography());
        return this;
    }

    @Step("Редактируем поле названия художника")
    public EditArtistPage editNameArtist(String namePainting) {
        nameInput.clear();
        nameInput.sendKeys(namePainting);
        return this;
    }

    @Step("Редактируем поле фотографии художника")
    public EditArtistPage editPhotoArtist(String urlFile) {
        photoInput.sendKeys(new File(urlFile).getAbsolutePath());
        return this;
    }

    @Step("Редактируем поле описания художника")
    public EditArtistPage editBiographyArtist(String biographyArtist) {
        biographyTextarea.clear();
        biographyTextarea.sendKeys(biographyArtist);
        return this;
    }

    @Step("")
    public ArtistPage saveNewData() {
        saveButton.click();
        return new ArtistPage();
    }

    @Step("а")
    public ArtistCardPage saveUpdateData() {
        saveButton.click();
        return new ArtistCardPage();
    }

    @Step("Сохраняем карточку художника")
    public EditArtistPage clickSave() {
        saveButton.click();
        return this;
    }

    @Step("Не сохраняем карточку художника")
    public ArtistPage cancelSaveData() {
        cancelButton.click();
        return new ArtistPage();
    }
}
