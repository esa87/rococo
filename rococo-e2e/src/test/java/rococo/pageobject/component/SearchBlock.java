package rococo.pageobject.component;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class SearchBlock extends BaseComponent<SearchBlock>{
    public SearchBlock() {
        super($("main[id='page-content'] div.justify-center:nth-child(2)"));
    }

    public SearchBlock search(String searchText){
        self.$("input").sendKeys(searchText);
        self.$("button").click();
        return this;
    }


}
