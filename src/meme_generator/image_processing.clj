(ns meme-generator.image-processing
  (:use mikera.image.core)
  (:require [meme-generator.config :refer :all]
            [meme-generator.common :refer :all]
            [me.raynes.fs :refer [base-name]]
            [clojure.java.io :refer [resource file input-stream as-file]]
            [mikera.image.filters :as filt]))

(defn get-params [params]
  (merge image-defaults (select-keys params allowed-form-keys)))

(defn font-style-selector [font-style]
  (case font-style
        "bold" java.awt.Font/BOLD
        "regular" java.awt.Font/PLAIN
        "italic" java.awt.Font/ITALIC
        java.awt.Font/PLAIN))

(defn positionings [image-width image-height string-width string-height]
  (let [padding text-padding
        half-string-height (quot string-height 2)]
        {:center-x (quot (- image-width string-width) 2)
        :center-y (quot (+ image-height half-string-height) 2)
        :left padding
        :right (- image-width string-width padding)
        :top (+ string-height padding)
        :bottom (- image-height padding half-string-height)}))

(defn get-position-by-definition [{:keys [horizontal vertical]} image-width image-height string-width string-height]
  (let [positionings (positionings image-width image-height string-width string-height)]
    {:position-x (if (= horizontal "center")
                   (:center-x positionings)
                   (if-let [pos-x ((keyword horizontal) positionings)]
                     pos-x
                     (max 0 (read-string horizontal))))
     :position-y (if (= vertical "center")
                   (:center-y positionings)
                   (if-let [pos-y ((keyword vertical) positionings)]
                     pos-y
                     (max 0 (read-string vertical))))}))

(defn get-position [image text font position]
  (let [font-metrics (.getFontMetrics (.getGraphics image) font)
        string-width (.stringWidth font-metrics text)
        string-height (.getHeight font-metrics)
        imageWidth (.getWidth image)
        imageHeight (.getHeight image)]
    (get-position-by-definition position imageWidth imageHeight string-width string-height)))

(defn get-params [params]
  (merge image-defaults (select-keys params allowed-form-keys)))

(defn add-text-outline-effect [image, {:keys [text, size, font, font-color, with-outline, font-style, position-x, position-y]}]
  (if (= "1" with-outline)
    (let [imageGraphics (.getGraphics image)
          fontObject (java.awt.Font. font (font-style-selector font-style) (read-string size))
          positions (get-position image text fontObject {:vertical position-y :horizontal position-x})]
      (.setFont imageGraphics fontObject)
      (.setColor imageGraphics (java.awt.Color/decode text-outline-color))
      (.drawString imageGraphics text (+ 2 (positions :position-x)) (+ 2 (positions :position-y)))
      (.drawString imageGraphics text (+ 3 (positions :position-x)) (+ 3 (positions :position-y)))
      (.drawString imageGraphics text (+ 4 (positions :position-x)) (+ 4 (positions :position-y)))

      (.drawString imageGraphics text (- (positions :position-x) 2) (- (positions :position-y) 2))
      (.drawString imageGraphics text (- (positions :position-x) 3) (- (positions :position-y) 3))
      (.drawString imageGraphics text (- (positions :position-x) 4) (- (positions :position-y) 4))))
  image)

(defn add-text-line [image, text, size, font, font-color, with-outline, font-style, position-x, position-y]
  (let [imageGraphics (.getGraphics image)
        fontObject (java.awt.Font. font (font-style-selector font-style) (read-string size))
        positions (get-position image text fontObject {:vertical position-y :horizontal position-x})]
    (.setFont imageGraphics fontObject)
    (.setColor imageGraphics (java.awt.Color/decode font-color))
    (.drawString imageGraphics text (positions :position-x) (positions :position-y))
    image))

(defn add-text [image {:keys [text, size, font, font-color, with-outline, font-style, position-x, position-y]}]
  (add-text-line image, text, size, font, font-color, with-outline, font-style, position-x, position-y))

(defn resize-image [image]
  (resize image max-width))

(defn save-image [image, newPath]
  (save image newPath))

(defn process-image [image params]
  (let [parsed-params (get-params params)]
    (->
        image
        load-image
        resize-image
        (add-text-outline-effect parsed-params)
        (add-text parsed-params)
        (save-image (apply str memesFolder (new-uuid) ".png")))))

(defn stream-image [imagePath]
  (response-with-status 200 (input-stream (str memesFolder imagePath)) "image/png"))

(defn create-meme [params]
  (let [imageFileName (:image params)
        imagePath (str imagesFolder imageFileName)
        imageFile (resource (str "images/" imageFileName))]
    (if-not (nil? imageFile)
      (let [processed (process-image imageFile params)]
        (base-name processed))
      nil)))

(defn available-memes []
  (let [files (filter #(.isFile %)
                (file-seq
                  (clojure.java.io/file imagesFolder)))]
  (into [] (map #(base-name (.getPath %)) files))))

(defn get-all-fonts []
  (let [graphics-environment (java.awt.GraphicsEnvironment/getLocalGraphicsEnvironment)
        fonts (.getAvailableFontFamilyNames graphics-environment)]
    (into [] fonts)))

(defn get-meme-path [imagePath]
  (str memesFolder imagePath))

(defn meme-exists [imagePath]
   (let [meme-path (get-meme-path imagePath)]
    (if (.exists (as-file meme-path))
      true
      false)))
