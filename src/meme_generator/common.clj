(ns meme-generator.common)

(defn current-timestamp []
  (quot (System/currentTimeMillis) 1000))

(defn new-uuid []
      (java.util.UUID/randomUUID))

(defn response-with-status [status response content-type]
  {:status status :headers {"Content-Type" content-type} :body response})
