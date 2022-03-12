package com.ipiecoles.java.eval.th330.controller;

import com.ipiecoles.java.eval.th330.model.Album;
import com.ipiecoles.java.eval.th330.service.AlbumService;
import com.ipiecoles.java.eval.th330.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class AlbumController {
    @Autowired
    private AlbumService albumService;
    @Autowired
    private ArtistService artistService;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/albums/{idArtist}/delete/{idAlbum}"
    )
    public RedirectView deleteAlbum(@PathVariable Long idArtist, @PathVariable Long idAlbum) {
        albumService.deleteAlbum(idAlbum);
        return new RedirectView("/artists/" + idArtist);
    }

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/albums/new/{idArtist}",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    public RedirectView newAlbum(Album album, @PathVariable Long idArtist) {
        album.setArtist(artistService.findById(idArtist));
        albumService.creerAlbum(album);
        //Redirection vers /employes/id
        return new RedirectView("/artists/" + idArtist);
    }
}
