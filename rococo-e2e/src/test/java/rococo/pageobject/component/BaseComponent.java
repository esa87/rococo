package rococo.pageobject.component;

import com.codeborne.selenide.SelenideElement;

import javax.annotation.Nonnull;

public abstract class BaseComponent <T extends BaseComponent<?>>{
    protected final SelenideElement self;

    protected BaseComponent(SelenideElement self) {
        this.self = self;
    }

    @Nonnull
    public SelenideElement getSelf(){
        return self;
    }
}
