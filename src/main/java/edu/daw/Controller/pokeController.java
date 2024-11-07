package edu.daw.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Random;

@Controller
public class pokeController {

    @GetMapping("/")
    public String id( Model model) {
        Random r = new Random();
        int id = r.nextInt(151) + 1;

        String URL = "https://pokeapi.co/api/v2/pokemon/" + id;

        RestTemplate restTemplate = new RestTemplate();

        try {
            Map<String, Object> result = restTemplate.getForObject(URL, Map.class);

            if (result != null) {
                model.addAttribute("id", id);

                String nombre = (String) result.get("name");
                model.addAttribute("nombre", nombre);

                List<Map<String, Object>> habilidades = (List<Map<String, Object>>) result.get("abilities");
                String habilidad = ((Map<String, Object>) habilidades.get(0).get("ability")).get("name").toString();
                model.addAttribute("habilidad", habilidad);

                Map<String, Object> sprites = (Map<String, Object>) result.get("sprites");
                String imagen = (String) sprites.get("front_default");
                model.addAttribute("imagen", imagen);

                String pista = reemplazarVocales(nombre);
                model.addAttribute("pista", pista);

            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "inicio";
    }
    @PostMapping("/play")
    @ResponseBody
    public String adivinaPokemon(@RequestParam("nombreIntroducido") String nombreIntroducido,
                                 @RequestParam("nombrePokemon") String nombrePokemon) {
        if (nombreIntroducido.trim().equalsIgnoreCase(nombrePokemon)) {
            return "¡Has acertado! El Pokémon es " + nombrePokemon;
        } else {
            return "¡Has fallado! El Pokémon es " + nombrePokemon;
        }
    }

    @GetMapping("/new")
    public String newPlay(Model model) {
        return "redirect:/";
    }

    private String reemplazarVocales(String texto) {
        // Usamos reemplazo con expresiones regulares para todas las vocales
        return texto.replaceAll("[aeiouAEIOU]", "_");
    }
}

