package com.devertix.shoppinglist;

import com.devertix.dao.GroceryItemDAO;
import com.devertix.model.GroceryItem;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class App {
    private static String getHostname() {
        String hostname = null;
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            hostname = "failed-to-get-hostname";
        }
        return hostname;
    }

    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public", Location.CLASSPATH);
        }).start(7000);
        GroceryItemDAO groceryItemDAO = new GroceryItemDAO();
        app.get("/api/grocery-items", ctx -> {
            List<GroceryItem> allGroceryItems = groceryItemDAO.getAllGroceryItems();
            ctx.json(allGroceryItems);
        });
        app.post("/api/grocery-items", ctx -> {
            GroceryItem groceryItem = ctx.bodyAsClass(GroceryItem.class);
            groceryItemDAO.addGrocery(groceryItem);
            ctx.json(groceryItem);
            ctx.status(201);
        });
        app.delete("/api/grocery-items/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            boolean success = groceryItemDAO.removeGroceryItemById(id);
            if (success) {
                ctx.status(200).result("Grocery item deleted successfully");
            } else {
                ctx.status(404).result("Grocery item not found");
            }
        });
    }
}
