if (op.hasInsert()) {
final CommandInsert request = op.getInsert();
final JsonObject content = JsonObject.fromJson(request.getContentJson());
final Collection collection =
        connection.getBucket(request.getDocId().getBucketName())
        .scope(request.getDocId().getScopeName())
        .collection(request.getDocId().getCollectionName());
        String docId = request.getDocId().getDocId() // get docId of the document

        ctx.insert(collection,docId,content);
        });
        }

// Reads the transaction object to see if the tests needs a replace operation
if (op.hasReplace()) {
final CommandReplace request = op.getReplace();
        
//Performer gets the new content value of the document
final JsonObject content = JsonObject.fromJson(request.getContentJson());
        
// Retrieve information on the location of the document
final Collection collection =
        connection.getBucket(request.getDocId().getBucketName())
        .scope(request.getDocId().getScopeName())
        .collection(request.getDocId().getCollectionName());
        String docId = request.getDocId().getDocId() // get docId of the document
                
//Finally execute transaction operation
final TransactionGetResult r = ctx.get(collection, );
        ctx.replace(r, content);
        }
        });
