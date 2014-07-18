package controllers;

import models.Quip;
import models.QuipDto;
import ninja.Context;
import ninja.FilterWith;
import ninja.Result;
import ninja.Results;
import ninja.SecureFilter;
import ninja.params.PathParam;
import ninja.validation.JSR303Validation;
import ninja.validation.Validation;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import dao.QuipDao;
import etc.LoggedInUser;

@Singleton
public class QuipController {
    
    @Inject
    QuipDao quipDao;

    ///////////////////////////////////////////////////////////////////////////
    // Show quip
    ///////////////////////////////////////////////////////////////////////////
    public Result quipShow(@PathParam("id") Long id) {

        Quip quip = null;

        if (id != null) {

            quip = quipDao.getQuip(id);
            if (quip == null)
                return Results.redirect("/");

        }

        return Results.html().render("quip", quip);

    }
    
    ///////////////////////////////////////////////////////////////////////////
    // Create new quip
    ///////////////////////////////////////////////////////////////////////////
    //@FilterWith(SecureFilter.class)
    public Result quipNew() {

        return Results.html();

    }

    //@FilterWith(SecureFilter.class)
    public Result quipNewPost(@LoggedInUser String username,
                              Context context,
                              @JSR303Validation QuipDto quipDto,
                              Validation validation) {

        if (validation.hasViolations()) {

            context.getFlashCookie().error("Please correct field.");
            context.getFlashCookie().put("sourceDate", quipDto.sourceDate);
            context.getFlashCookie().put("content", quipDto.content);

            return Results.redirect("/quip/new");

        } else {
            
            quipDao.postQuip(username, quipDto);
            
            context.getFlashCookie().success("New quip created.");
            
            return Results.redirect("/");

        }

    }

}
