var DropZone = function () {

     var Init = function() {
         Dropzone.options.myDropzone = {
             dictDefaultMessage: "",
             init: function() {
                 this.on("addedfile", function(file) {
                     // Create the remove button
                     var removeButton = Dropzone.createElement("<a href='javascript:;'' class='btn red btn-sm btn-block'>Remove</a>");

                     // Capture the Dropzone instance as closure.
                     var _this = this;

                     // Listen to the click event
                     removeButton.addEventListener("click", function(e) {
                         // Make sure the button click doesn't submit the form:
                         e.preventDefault();
                         e.stopPropagation();

                         // Remove the file preview.
                         _this.removeFile(file);
                         // If you want to the delete the file on the server as well,
                         // you can do the AJAX request here.
                     });

                     // Add the button to the file preview element.
                     file.previewElement.appendChild(removeButton);
                 });
             }
         }

         //获取angular中id为hideuserid的标签所在的scope
         var userid = angular.element(hideuserid).scope().token.user.id;
         $("#dropz").dropzone({
             url: "mvc/FileUpload?method=upload&userid="+userid,
             addRemoveLinks: true,
             dictRemoveLinks: "x",
             dictCancelUpload: "x",
             maxFiles: 10,
             maxFilesize: 5,
             acceptedFiles: "",
             init: function() {
                 this.on("success", function(file) {
                     console.log("File " + file.name + "uploaded");
                 });
                 this.on("removedfile", function(file) {
                     console.log("File " + file.name + "removed");
                 });
             }
         });
    }


    return {
        //main function to initiate the module
        init: function () {
            Init();
        }

    };

}();
