import com.structurizr.Workspace;
import com.structurizr.io.StructurizrWriterException;
import com.structurizr.io.json.JsonWriter;
import com.structurizr.model.*;
import com.structurizr.view.ContainerView;
import com.structurizr.view.SystemContextView;
import com.structurizr.view.ViewSet;

import java.io.StringWriter;

public class SpotterArchitectureOverview {

    private final Workspace workspace;
    private final SoftwareSystem theEye;

    public SpotterArchitectureOverview() {
        workspace = new Workspace("the-eye.pl", "asdf");
        Model model = workspace.getModel();


        // create the various types of people (roles) that use the software system

        Person spotter = model.addPerson(Location.External, "Spotter", "A user who is executing missions.");
        Person client = model.addPerson(Location.External, "Business Client", "A business who see reports of the ");
        Person admin = model.addPerson(Location.External, "Administration User", "A system administration user who creates and manages missions");

        theEye = model.addSoftwareSystem(Location.Internal, "the-eye.pl", "the-eye.pl is a crowd verification platform");
        SoftwareSystem googleMaps = model.addSoftwareSystem(Location.External, "Google Maps", "maps.google.com");

        client.uses(theEye, "See reports of finished missions");
        spotter.uses(theEye, "List, accept, do missions");
        admin.uses(theEye, "Manages missions");

        theEye.uses(googleMaps, "Geocoding locations, displays maps.");

        // components view

        Container mongoDB = theEye.addContainer("Database", "", "MongoDb");
        Container webApi = theEye.addContainer("Web/Api", "", "Angularjs, ExpressJs");
        Container mobileApp = theEye.addContainer("MobileApp", "", "Ionic, Phonegap");
        Container imageStorage = theEye.addContainer("Images store", "", "S3 bucket");

        webApi.uses(mongoDB, "store/read data");
        webApi.uses(googleMaps, "geocode locations");
        webApi.uses(imageStorage, "show picture");

        mobileApp.uses(webApi, "list and manages missions");
        mobileApp.uses(googleMaps, "display locations on map");
        mobileApp.uses(imageStorage, "upload pictures");

        spotter.uses(mobileApp, "");
        client.uses(webApi, "");
        admin.uses(webApi, "");


    }

    private void createContextView() {
        ViewSet viewSet = workspace.getViews();
        SystemContextView contextView = viewSet.createContextView(theEye);
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();
    }

    private void createContainerView() {
        ViewSet viewSet = workspace.getViews();
        ContainerView containerView = viewSet.createContainerView(theEye);
        containerView.addAllSoftwareSystems();
        containerView.addAllContainers();
        containerView.addAllPeople();
    }


    public static void main(String[] args) throws StructurizrWriterException {

        SpotterArchitectureOverview spotterArchitecture = new SpotterArchitectureOverview();

        spotterArchitecture.createContextView();
        spotterArchitecture.createContainerView();

        JsonWriter jsonWriter = new JsonWriter(true);
        StringWriter stringWriter = new StringWriter();
        jsonWriter.write(spotterArchitecture.workspace, stringWriter);
        System.out.println(stringWriter.toString());

    }


}
