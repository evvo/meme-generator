(ns meme-generator.templates.main
  (:require [hiccup.form :as f]
            [meme-generator.config :refer :all]
            [hiccup.page :as hp]))

(defn template-header [title]
;;   TODO add js build
  [:head
   [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
   (hp/include-css "/lib/ink/css/ink-flex.min.css")
   (hp/include-css "/lib/ink/css/font-awesome.min.css")
   (hp/include-css "/lib/spectrum/spectrum.css")
   (hp/include-css "/custom/style.css")
   (hp/include-js "/lib/ink/js/holder.js")
   (hp/include-js "/lib/ink/js/ink-all.min.js")
   (hp/include-js "/lib/ink/js/autoload.js")
   (hp/include-js "/lib/jquery/jquery.min.js")
   (hp/include-js "/lib/spectrum/spectrum.js")
   (hp/include-js "/custom/script.js")
   [:title (str main-title " | " title)]])

(defn template-footer []
  [:footer {:class "clearfix"}
   [:div {:class "ink-grid"}
     [:ul {:class "unstyled inline half-vertical-space"}
        [:li {:class "active"}
         [:a {:href "/"} "Home"]]]
     [:p {:class "note"}
      "All rights reserved. Made using Clojure by Evtimiy Mihaylov (evo@vaupe.com)"]]])

(defn main-header-element []
  [:header
    [:h1 "Meme Generator"]
    [:nav {:class "ink-navigation"}
     [:ul {:class "menu horizontal black"}
      [:li {:class "active"}
       [:a {:href "/"} "Home"]]]]])

(defn body [content]
  [:div {:class "ink-grid"}
   (main-header-element)
   content])

(defn template-prototype [title content custom-body-class]
  (hp/html5
    (template-header title)
    (body [:div {:class (if custom-body-class custom-body-class custom-body-class)} content])
    (template-footer)))

(defn template
  ([title content]
   (template-prototype title content "column-group gutters"))
  ([title content custom-body-class]
   (template-prototype title content custom-body-class)))

(defn get-errors-markup [errors]
  (into [] (cons :ul
    (mapv
      (fn [error-field]
         [:li (first error-field)
            (into [] (cons :ul (mapv (fn [error] [:li error]) (last error-field))))
          ]
        )
      errors))))

(defn errors-template [validator-result]
  (template
    "Errors occured!"
    [:div {:class "all-100"}
      (get-errors-markup validator-result)] "column-group"))
