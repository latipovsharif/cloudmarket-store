package com.vvmarkets.components;

import com.vvmarkets.core.IListContent;
import com.vvmarkets.core.ListUtil;
import com.vvmarkets.core.Utils;
import com.vvmarkets.dao.Product;
import com.vvmarkets.dao.ProductProperties;
import com.vvmarkets.services.ProductService;
import com.vvmarkets.services.RestClient;
import com.vvmarkets.utils.ResponseBody;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ProductComponent extends VBox {

    public IListContent getProduct() {
        return product;
    }

    public void setProduct(IListContent product) {
        this.product = product;
    }

    private IListContent product;

    public ProductComponent(IListContent product) {
        this.product = product;
    }

    public static List<ProductComponent> getList() {
        return listToComponent(ListUtil.INSTANCE.fillMain());
    }

    public static List<ProductComponent> getList(String productId) {
        return listToComponent(ListUtil.INSTANCE.listForCategory(productId, false));
    }

    public static List<ProductComponent> getSearchList(String searchString) {
        ProductService productService = RestClient.getClient().create(ProductService.class);
        Call<ResponseBody<List<ProductProperties>>> listProductForCategoryCall = productService.productSearch(searchString);

        try{
            Response<ResponseBody<List<ProductProperties>>> response = listProductForCategoryCall.execute();
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    if (response.body().getStatus() == 0) {
                        return listToComponent(response.body().getBody());
                    }
                }
            }
        } catch (Exception e) {
            Utils.logException(e, "error while handling response of category for product list");
        }
        return new ArrayList<>();
    }

    private static List<ProductComponent> listToComponent(List<? extends IListContent> contents){
        List<ProductComponent> res = new ArrayList<>();

        if (contents == null) {
            return res;
        }

        try {
            CopyOnWriteArrayList<IListContent> lst = new CopyOnWriteArrayList<>(contents);

            for (IListContent content : lst) {
                if (content == null) {
                    continue;
                }

                ProductComponent pc = new ProductComponent(content);
                pc.setBackground(new Background(new BackgroundFill(Paint.valueOf("#fff"), null, null)));
                ImageView iv = new ImageView();
                iv.setImage(new Image("images/no_image.png"));
                Label lbl = new Label(content.getName());
                pc.getChildren().add(iv);
                pc.getChildren().add(lbl);

                res.add(pc);
            }
        }catch(Exception e) {
            Utils.logException(e, "cannot get list of content");
        }
        return res;
    }
}
