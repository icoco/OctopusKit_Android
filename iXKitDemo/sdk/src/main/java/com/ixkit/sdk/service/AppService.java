package com.ixkit.sdk.service;


import com.ixkit.octopus.core.WebMethod;
import com.ixkit.octopus.service.BaseService;
import com.ixkit.octopus.util.Argument;




public class AppService extends BaseService {

    /*
     * api: http://www.ixkit.com/api/app
     * method: get
     * purpose: list all app
     */
    public static AppService list(
            String filter,
            int pageIndex) {
        Argument argument = Argument.create(
                "filter", filter,
                "pageIndex", pageIndex
        );

        AppService service = new AppService();

        String route = "app";
        service.setRoute(route)
                .setMethod(WebMethod.Get)
                .setRequestParameter(argument.getData());


        return service;
    }

    /*
     * api: http://www.ixkit.com/api/app/13
     * method: get
     * purpose: show app detail
     */
    public static AppService detail(String id, Class responseClass) {

        Argument argument = Argument.create(
                "id", id
        );

        AppService service = new AppService();

        String route = "app/" + id;
        service.setRoute(route)
                .setMethod(WebMethod.Get)
                .setResponseClass(responseClass);
               // .setRequestParameter(argument.getData());

        return service;
    }


    /*
     * api: http://www.ixkit.com/api/app/
     * method: post
     * purpose: create a new app data
     */
    public static AppService create(String name) {

        Argument argument = Argument.create(
                "name", name
        );

        AppService service = new AppService();

        String route = "app";
        service.setRoute(route)
                .setMethod(WebMethod.Post)
                .setRequestParameter(argument.getData());

        return service;
    }

    /*
     * api: http://www.ixkit.com/api/app/15
     * method: delete
     * purpose: delete a app data by id
     */
    public static AppService delete(String id) {

        Argument argument = Argument.create(
                "id", id
        );

        AppService service = new AppService();

        String route = "app/" + id;
        service.setRoute(route)
                .setMethod(WebMethod.Delete);
                //.setRequestParameter(argument.getData());

        return service;
    }
}
