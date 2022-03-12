package com.ipiecoles.java.eval.th330.controller;

import com.ipiecoles.java.eval.th330.model.Album;
import com.ipiecoles.java.eval.th330.model.Artist;
import com.ipiecoles.java.eval.th330.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Controller
public class ArtistController {

    @Autowired
    private ArtistService artistService;


    @RequestMapping(value = "/artists/{id}")
    public ModelAndView detailArtist(@PathVariable("id") Long id) {
        ModelAndView model = new ModelAndView("detailArtist");
        try {
            model.addObject("artist", artistService.findById(id));
            return model;
        } catch (Exception e) {
            throw new ResponseStatusException(NOT_FOUND, "Unable to find resource");
        }
    }

    @RequestMapping(value = "/artists")
    public ModelAndView listeArtists(@RequestParam Optional<String> name, @RequestParam Integer page, @RequestParam Integer size, @RequestParam String sortProperty, @RequestParam String sortDirection){
        ModelAndView model = new ModelAndView("listeArtists");
        Page<Artist> artists;
        String nom = "";
        if (name.isPresent()) {
            nom = name.get();
            artists = artistService.findByNameLikeIgnoreCase(nom, page, size, sortProperty, sortDirection);
        } else {
            artists = artistService.findAllArtists(page, size, sortProperty, sortDirection);
        }
        model.addObject("artists", artists);
        model.addObject("nbArtists", artists.getTotalElements());
        model.addObject("name", nom);
        model.addObject("start", page * size + 1);
        model.addObject("end", page * size + artists.getNumberOfElements());
        model.addObject("sortProperty", sortProperty);
        model.addObject("sortDirection", sortDirection);
        return model;
    }

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/artists",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    public RedirectView newAlbum(Artist artist) {
        if(artist.getId() == null){
            //Création
            artist = artistService.creerArtiste(artist);
        }
        else {
            //Modification
            artist = artistService.updateArtiste(artist.getId(), artist);
        }
        //Redirection vers /employes/id
        return new RedirectView("/artists/" + artist.getId());
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/artists/{id}/delete"
    )
    public RedirectView deleteArtist(@PathVariable Long id) {
        artistService.deleteArtist(id);
        return new RedirectView("/artists?page=0&size=10&sortProperty=name&sortDirection=ASC");
    }
}
