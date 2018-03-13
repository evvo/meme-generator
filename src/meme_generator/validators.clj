(ns meme-generator.validators
  (:require
    [metis.core :refer :all]
    [meme-generator.image-processing :refer :all]))

(defn image-for-meme-exists [map key options]
  (if-not (.contains (available-memes) (map key))
    "No such image"))

(defn validate-font-exists [map key options]
  (if-not (.contains (get-all-fonts) (map key))
    "No such font"))

(defn validate-font-style [map key options]
  (if-not (.contains ["regular" "italic" "bold"] (map key))
    "No such font style"))

(defn validate-position-x [map key options]
  (if-not (.contains ["left" "center" "right"] (map key))
    "No such position x"))

(defn validate-position-y [map key options]
  (if-not (.contains ["top" "center" "bottom"] (map key))
    "No such position y"))

(defn validate-color [map key options]
  (if-not (re-matches #"#[0-9a-f]{6}|[0-9a-f]{3}" (map key))
    "Invalid color"))

(defvalidator meme-validator
  [:text [:presence :length {:less-than 50}]]
  [:font-color :validate-color]
  [:image :image-for-meme-exists]
  [:size :numericality {:less-than-or-equal-to 100}]
  [:font :validate-font-exists]
  [:font-style :validate-font-style]
  [:position-x :validate-position-x]
  [:position-y :validate-position-y])
