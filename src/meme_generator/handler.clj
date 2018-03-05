(ns meme-generator.handler
  (:use hiccup.core)
  (:use ring.util.response)
  (:require [meme-generator.config :refer :all]
            [meme-generator.common :refer :all]
            [meme-generator.templates.main :refer :all]
            [meme-generator.templates.meme-form :refer :all]
            [meme-generator.image-processing :refer :all]
            [meme-generator.validators :refer [meme-validator]]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [clojure.java.io :refer [resource input-stream as-file]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defn get-meme-by-id [imagePath]
  (template "Your new meme!"
    [:section {:class "all-100 align-center"}
     [:div {:class "all-100"}
      [:p "This is your new meme!"]
        [:img {:src (str "/meme-image/" imagePath)}]]]))

(defn get-meme-image [imagePath]
  (let [filePath (str memesFolder imagePath)]
    (if (.exists (as-file filePath))
      (response-with-status 200 (input-stream (str memesFolder imagePath)) "image/jpg")
      (response-with-status 404 "Not found" "text/html"))))

(defn create-meme-image [params]
  (let [processed (create-meme params)]
    (if processed
      (redirect (str "/meme/" processed))
      (response-with-status 404 "Not Found" "text/html"))))

(defn create-meme-request [{:keys [params]}]
  (let [validator-result (meme-validator params)]
    (if (empty? validator-result)
      (create-meme-image params)
      (response-with-status 422 (template "Errors occured!" [:div (get-errors-markup validator-result)]) "text/html"))))

(defroutes app-routes
  (GET "/" [request] create-meme-form)
  (POST "/create-meme/new" [request] create-meme-request)
  (GET "/meme/:id" [id] (get-meme-by-id id))
  (GET "/meme-image/:imagePath" [imagePath] (get-meme-image imagePath))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
