<#import "layout.ftl" as u>
<@u.layout title="Upload file">
    <form method="POST" action="/upload" enctype="multipart/form-data">
        <div>
            <label>
                Select .XML files to upload
                <input type="file" name="filesToUpload" accept=".xml" multiple>
            </label>
        </div>
        <div>
            <input type="submit" value="Upload" name="submit">
        </div>
    </form>

</@u.layout>
