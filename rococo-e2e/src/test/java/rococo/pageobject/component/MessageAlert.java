package rococo.pageobject.component;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class MessageAlert extends BaseComponent<MessageAlert> {
    private final SelenideElement closeButton = $("button.btn-icon-sm");
    private final SelenideElement textMessage = $("div.text-base");

    public MessageAlert() {
        super($("div[role='alertdialog']"));
    }

    public MessageAlert closeMessage() {
        self.$("button.btn-icon-sm").click();
        return this;
    }

    public String getMessageText() {
        return self.$("div.text-base").getText();
    }
}
