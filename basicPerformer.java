if (op.hasInsert()) {
//Transaction Insert logic
}

if (op.hasRemove()) {
//Transaction Remove logic
}

if (op.hasGet()) {
//Transaction Get logic
}

//The example used in the blog
// Reads the transaction object to see if the tests needs a replace operation
if (op.hasReplace()) {
final CommandReplace request = op.getReplace();
        
//Performer gets the new content value of the document
final JsonObject content = JsonObject.fromJson(request.getContentJson());
        
// Retrieve the document on which we need to execute the transaction
final Collection collection =
        connection.getBucket(request.getDocId().getBucketName())
        .scope(request.getDocId().getScopeName())
        .collection(request.getDocId().getCollectionName());
        String docId = request.getDocId().getDocId() // get docId of the document
                
//Finally execute transaction operation
final TransactionGetResult r = ctx.get(collection, docId);
ctx.replace(r, content);
       
