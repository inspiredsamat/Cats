package kz.inspiredsamat.cats.controller;

import com.google.gson.Gson;
import kz.inspiredsamat.cats.model.CatImage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@Controller
public class MainController {

    @GetMapping("/random")
    public String getRandomCatImage(Model model) {
        List<CatImage> images = Arrays.asList(getImage(1));
        model.addAttribute("images", images);
        return "hello";
    }

    @GetMapping("/random/{n}")
    public String getNRandomCatImages(@PathVariable int n, Model model) {
        List<CatImage> images = Arrays.asList(getImage(n));
        model.addAttribute("images", images);
        return "hello";
    }


    private CatImage[] getImage(int n) {
        try {
            String urlString = n == 1 ? "https://api.thecatapi.com/v1/images/search" :
                    "https://api.thecatapi.com/v1/images/search?limit=" + n;
            URL url = new URL(urlString);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {
                StringBuilder informationString = new StringBuilder();
                Scanner scanner = new Scanner(url.openStream());
                while (scanner.hasNext()) {
                    informationString.append(scanner.nextLine());
                }
                scanner.close();
                Gson json = new Gson();
                return json.fromJson(informationString.toString(), CatImage[].class);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
