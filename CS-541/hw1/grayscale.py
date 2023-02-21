#matrix manipulation - convert color image to grayscale
#do not make changes in the function names
#do not use any inbuilt libraries and/or packages to convert image to grayscale, implement the code yourself

from matplotlib.image import imread
from matplotlib import pyplot as plt

INPUT_IMAGE = 'colorImage.jpg'
OUTPUT_IMAGE = 'grayscaleImage.jpg'

def get_grayscale_val_from_rgb(r: int, g:int, b:int)-> int:
    return (0.2989 * r + 0.5870 * g + 0.1140 * b)

def to_grayscale(image: str) -> None:
    
    image_rgb = imread(image)
    height, width, channels = image_rgb.shape
    
    image_gray_scale = [
        [0 for _ in range(width)] for __ in range(height)
    ]
    
    for h in range(height):
        for w in range(width):
            r, g, b = image_rgb[h][w]
            image_gray_scale[h][w] = get_grayscale_val_from_rgb(r, g, b) 

    plt.imsave(OUTPUT_IMAGE, image_gray_scale, cmap="gray")

def main():
    #use the already given image only for submission
    #Load the colored image here
    #save the grayscale image as grayscale_image in same folder
    to_grayscale(INPUT_IMAGE)

if __name__ == "__main__":
    main()
