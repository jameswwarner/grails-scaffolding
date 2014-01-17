<%-- Copyright (c) 2013-2014 James W. Warner, All Rights Reserved --%>
<div id=dialog-admin-img class=dialog data-dialog-width=550 title="Set Image (Image Library)">
    <label>
        <span>Search:</span>
        <div>
            <input name=search placeholder="Find existing images"
                autocomplete="off" type="text" />
        </div>
    </label>
    <div>
        <h4>Upload</h4>
        <uploader:uploader id="dialogImageUploader" url="${createLink([mapping: 'rest', controller:'image', action:'upload'])}">
            <uploader:onComplete>
                admin.brand.handleImageUpload(responseJSON.image);
            </uploader:onComplete>
        </uploader:uploader>
    </div>
    <div id=image-create class=space-below-2x>
        <h4>Enter Image Url</h4>
        <input id="image-url" placeholder="Enter image Url" autocomplete="off" type="text" />
        <input type=button value=Create />
    </div>
    <div id=image-preview class=admin-edit-pane data-type=image data-action=update>
        <input type=hidden class=admin-data name=id />
        <div>
            <!-- img tag will be here -->
        </div>
        <label>
            <span>Name:</span>
            <input type=text name=name class=admin-data />
        </label>
        <label>
            <span>Tags:</span>
            <ul class=tag-list>
            </ul>
        </label>
    </div>
    <button name=save-img>Save</button>
</div>
