package com.example.starsgallery.service;

import com.example.starsgallery.beans.Star;
import com.example.starsgallery.dao.IDao;
import java.util.ArrayList;
import java.util.List;

public class StarService implements IDao<Star> {
    private List<Star> stars;
    private static StarService instance;

    private StarService() {
        stars = new ArrayList<>();
        seed();
    }

    public static StarService getInstance() {
        if (instance == null) instance = new StarService();
        return instance;
    }

    private void seed() {
        // Utilisation des images locales img1, img2, img3, img4 se trouvant dans res/drawable
        String path = "android.resource://com.example.starsgallery/drawable/";

        stars.add(new Star("Emma Watson", 
            path + "img1", 
            4.5f, 
            "Actrice britannique, célèbre pour son rôle d'Hermione Granger dans la saga Harry Potter. Elle est aussi une militante pour les droits des femmes.",
            "• 2001-2011 : Saga Harry Potter\n• 2012 : Le Monde de Charlie\n• 2017 : La Belle et la Bête"));

        stars.add(new Star("Tom Cruise", 
            path + "img2", 
            4.2f, 
            "Acteur et producteur américain. Connu pour ses rôles dans Top Gun et la série Mission Impossible, il réalise souvent ses propres cascades.",
            "• 1986 : Top Gun\n• 1996-2023 : Saga Mission Impossible\n• 2022 : Top Gun: Maverick"));

        stars.add(new Star("Scarlett Johansson", 
            path + "img3", 
            4.7f, 
            "Actrice américaine. Elle a incarné Black Widow dans l'univers Marvel et est l'une des actrices les mieux payées au monde.",
            "• 2003 : Lost in Translation\n• 2010-2019 : Marvel Cinematic Universe\n• 2019 : Marriage Story"));

        stars.add(new Star("Leonardo DiCaprio", 
            path + "img4",
            4.8f, 
            "Acteur américain oscarisé. Connu pour Titanic, Inception et Infiltrés. Il est très engagé dans la protection de l'environnement.",
            "• 1997 : Titanic\n• 2010 : Inception\n• 2015 : The Revenant"));
    }

    @Override public boolean create(Star o) { return stars.add(o); }
    @Override public boolean update(Star o) {
        for (Star s : stars) {
            if (s.getId() == o.getId()) {
                s.setName(o.getName());
                s.setImg(o.getImg());
                s.setStar(o.getStar());
                s.setDescription(o.getDescription());
                s.setFilmography(o.getFilmography());
                return true;
            }
        }
        return false;
    }
    @Override public boolean delete(Star o) { return stars.remove(o); }
    @Override public Star findById(int id) {
        for (Star s : stars) if (s.getId() == id) return s;
        return null;
    }
    @Override public List<Star> findAll() { return stars; }
}
