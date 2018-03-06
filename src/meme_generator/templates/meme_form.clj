(ns meme-generator.templates.meme-form
  (:require [hiccup.form :as f]
            [hiccup.page :as hp]
            [ring.util.anti-forgery :refer :all]
            [meme-generator.templates.main :refer :all]
            [meme-generator.image-processing :refer :all]))

(defn create-meme-form [request]
  (template "Create New Meme"
      (f/form-to {:class "ink-form all-100"} [:post "/create-meme/new"]
        (anti-forgery-field)
        [:fieldset
           [:div {:class "control-group required column-group gutters"}
            [:label {:class "all-20 small-30 tiny-30 medium-20 large-15 xlarge-15 align-right" :for "text"} "Text"]
             [:div {:class "control all-80 small-70 tiny-70 medium-75 large-80 xlarge-80"}
              (f/text-field :text "your meme text")]]]
         [:fieldset
           [:div {:class "control-group column-group gutters"}
            [:label {:class "all-20 small-30 tiny-30 medium-20 large-15 xlarge-15 align-right" :for "image"} "Meme Image"]
             [:div {:class "control all-80 small-70 tiny-70 medium-75 large-80 xlarge-80"}
              (f/drop-down :image (available-memes))]]]
         [:fieldset
           [:div {:class "column-group "}
            [:div {:class "control-group all-33 small-100 tiny-100"}
             [:div {:class "column-group gutters"}
              [:label {:class "all-30 align-right" :for "position-x"} "Font"]
               [:div {:class "control all-70"}
                (f/drop-down :font (get-all-fonts))]]]
            [:div {:class "control-group all-33 small-100 tiny-100"}
             [:div {:class "column-group gutters"}
              [:label {:class "all-30 align-right" :for "position-y"} "Size"]
               [:div {:class "control all-70"}
                (f/drop-down :size (range 20 85) 30)]]]
            [:div {:class "control-group all-33 small-100 tiny-100"}
             [:div {:class "column-group gutters"}
              [:label {:class "all-30 align-right" :for "position-y"} "Style"]
               [:div {:class "control all-70"}
                (f/drop-down :font-style ["bold" "italic" "regular"])]]]]]
          [:fieldset
           [:div {:class "column-group large-80 medium-80"}
            [:div {:class "column-group all-33 large-100 medium-100 small-100 tiny-100"}
             [:div {:class "control-group all-50 large-70 medium-70 small-50"}
               [:div {:class "column-group gutters"}
                [:label {:class "all-60 xlarge-55 align-right" :for "position-y"} "Color"]
                 [:div {:class "control all-40 xlarge-45"}
                  (f/text-field :font-color "#ffffff")]]]
              [:div {:class "control-group all-50 large-30 medium-30 small-50"}
               [:div {:class "column-group gutters"}
                [:label {:class "all-60 align-right" :for "position-y"} "Outline ?"]
                 [:div {:class "all-10"}
                  (f/check-box :with-outline false 1)]]]]
             [:div {:class "control-group all-33 large-100 medium-100 small-100 tiny-100"}
               [:div {:class "column-group gutters"}
                [:label {:class "all-30 align-right" :for "position-x"} "Horizontal"]
                 [:div {:class "control all-70"}
                  (f/drop-down :position-x ["left" "center" "right"])]]]
              [:div {:class "control-group all-33 large-100 medium-100 small-100 tiny-100"}
               [:div {:class "column-group gutters"}
                [:label {:class "all-30 align-right" :for "position-y"} "Vertical"]
                 [:div {:class "control all-70"}
                  (f/drop-down :position-y ["top" "center" "bottom"])]]]]]
          [:fieldset
           [:div {:class "control-group column-group all-100"}
             [:div {:class "control all-100"}
              (f/submit-button {:class "ink-button orange large-button" :value "GENERATE MEME"} :submit)]]])))
