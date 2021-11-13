package by.epam.finaltask.service;

import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;
import jakarta.servlet.http.Part;

public interface ImagesService {

    /**
     * @return context path of saved image
     */
    String saveImage(Part image, long userId) throws ServiceCanNotCompleteCommandRequest;
}
