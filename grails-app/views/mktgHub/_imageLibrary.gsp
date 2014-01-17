<%-- Copyright (c) 2013-2014 James W. Warner, All Rights Reserved --%>
<h2 class=space-below>Image Library</h2>

    <div class=block3>
        <h3>CREATE IMAGE</h3>
        <div id=uploader-id-home>
            <h4>Upload</h4>
            <uploader:uploader id="imageUploader" url="${[controller:'image', action:'upload']}">
                <uploader:onComplete>
                    editImageJson(responseJSON.image.id, responseJSON.image);
                    initializeImages();
                </uploader:onComplete>
            </uploader:uploader>
        </div>
        <!-- TODO: finish this at some point, but no one uses it right now -->
        <!-- div id=image-create>
            <h4>Or Enter Image Url</h4>
            <input id="image-url" placeholder="Enter image Url" autocomplete="off" type="text" />
            <button>Create</button>
        </div -->
        <div id=image-search>
            <h4>OR Search for existing image</h4>
            <label>
                <span>Search library:</span>
                <input id="image-search" placeholder="Find existing images" autocomplete="off" type="text" />
            </label>
        </div>
    </div>

    <div class=block4>
        <h3 class=space-below-sm>Recent Images</h3>
        <table id=image-list class=data-box-click>
            <thead>
                <tr>
                    <!-- TODO: make this a sortable table -->
                    <th data-col-name=name>Name</th>
                    <th data-col-name=path>Path</th>
                    <th data-col-name=created>Created</th>
                </tr>
            </thead>
            <tbody>
                <!-- rows will be put here by js -->
            </tbody>
        </table>
        <div id=current-image-pane class=space-above>
            <!-- img tag will be here -->
            <label>
                <span>Name:</span>
                <input type=text id=image-name>
            </label>
        </div>
    </div>
