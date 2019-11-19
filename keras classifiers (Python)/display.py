from tkinter import *

import numpy as np
import _thread, time
from math import *

#### __ define a pixel array object __ ####
class PixelArray:

    # constructor
    def __init__(self, image_res=None, pixels=None):

        if image_res is not None and pixels is None:
            self.image_res = image_res
            self.pixels = np.zeros((image_res, image_res))

        if pixels is not None:
            self.image_res = pixels.shape[0]
            self.pixels = pixels

    # get a pixel
    def __getitem__(self, X):
        return self.pixels[X]

    # set a pixel
    def __setitem__(self, X, value):
        self.pixels[X] = value

    # get nd array of pixels
    def get_ndarr(self):
        return self.pixels

    # image preprocessing methods

    # preprocess the image and returns as a pixel array
    def preprocess(self, new_res):
        new_pixels = self.normalize(new_res, 20)

        if new_pixels == None: return PixelArray(new_res)

        new_pixels = new_pixels.change_resolution(new_res)
        new_pixels = new_pixels.centre()

        return new_pixels

    # returns a pixel array of this image normalized
    def normalize(self, new_res, norm):
        (lower_x, lower_y, upper_x, upper_y) = self.bounding_box()
        (dx, dy) = (upper_x - lower_x, upper_y - lower_y)

        if dx < 1 or dy < 1: return

        z = max((dx/(norm*self.image_res/new_res), dy/(norm*self.image_res/new_res)))
        (dx0, dy0) = (dx/z, dy/z)

        new_pixels = np.zeros((self.image_res, self.image_res))

        for i in range(int(dx0)):
            for j in range(int(dy0)):
                if i < self.image_res and j < self.image_res:
                    new_pixels[i, j] = self.pixels[lower_x+int(round(i*z)), int(round(lower_y+j*z))]

        return PixelArray(pixels=new_pixels)

    # returns a pixel array of this image with resolution new_res
    def change_resolution(self, new_res):
        new_pixels = np.zeros((new_res, new_res))
        ratio = self.image_res//new_res

        for i in range(new_res):
            for j in range(new_res):

                total_color = 0
                for x in range(ratio):
                    for y in range(ratio):
                        total_color += self.pixels[i*ratio+x, j*ratio+y]
                new_pixels[i, j] = total_color/(ratio*ratio)

        return PixelArray(pixels=new_pixels)

    # returns a pixel array of this image centred
    def centre(self):
        (x, y, m) = (0, 0, 0)
        for i in range(self.image_res):
            for j in range(self.image_res):
                m += self.pixels[i, j];
                x += i*self.pixels[i, j];
                y += j*self.pixels[i, j];
        if m < 1: return self;

        (x, y) = (x/m, y/m)
        (dx, dy) = (int(x-self.image_res/2), int(y-self.image_res/2))

        new_pixels = np.zeros((self.image_res, self.image_res))

        for i in range(self.image_res):
            for j in range(self.image_res):
                if (i < self.image_res and j < self.image_res and i + dx < self.image_res and j + dy < self.image_res and 0 <= i + dx and 0 <= j + dy):
                    new_pixels[i, j] = self.pixels[i+dx, j+dy]
                else:
                    new_pixels[i, j] = 0;

        return PixelArray(pixels=new_pixels)

    # gets the smallest bounding box for the pixel array
    def bounding_box(self):
        (lower_x, lower_y, upper_x, upper_y) = (self.image_res, self.image_res, -1, -1)
        for i in range(self.image_res):
            for j in range(self.image_res):
                if self.pixels[self.image_res-i-1, self.image_res-j-1] > 0: lower_x = self.image_res-i-1;
                if self.pixels[self.image_res-j-1, self.image_res-i-1] > 0: lower_y = self.image_res-i-1;
                if self.pixels[i, j] > 0: upper_x = i;
                if self.pixels[j, i] > 0: upper_y = i;

        return (lower_x, lower_y, upper_x, upper_y)

#### __ define a display object __ ####
class Display:

    # constructor
    def __init__(self, image_res, pixel_size, title):

        # set display parameters
        self.image_res = image_res
        self.pixel_size = pixel_size

        self.display_res = image_res * pixel_size

        self.color_fg = '#%02x%02x%02x' % (0, 0, 0)
        self.color_bg = '#%02x%02x%02x' % (255, 255, 255)

        self.running = True

        # stores pixel data for access and processing
        self.pixels = PixelArray(image_res)

        # setup the class
        self._setup(title)
        
        # create the pixels
        self.pixel_rects = []
        for i in range(image_res):
            pixel_col = []
            for j in range(image_res):
                pixel_col.append(self.canvas.create_rectangle(i*pixel_size, j*pixel_size, (i+1)*self.pixel_size, (j+1)*self.pixel_size, width=0, fill=self.color_bg))
            self.pixel_rects.append(pixel_col)

        # close the canvas
        self.root.bind('<Escape>', lambda event: self.close())

    # setup the class
    def _setup(self, title):

        # create the root
        self.root = Tk()

        self.root.title(title)

        # set the root properties
        self.root.resizable(False, False)
        self.root.geometry(str(self.display_res) + 'x' + str(self.display_res))
        self.root.pack_propagate(False)

        # create and set canvas
        self.canvas = Canvas(self.root, width=self.display_res, height=self.display_res, highlightthickness=0, bg=self.color_bg)
        self.canvas.pack()

    # update the display
    def update(self):
        self.root.update()

    # reset the canvas
    def reset(self):
        for i in range(self.image_res):
            for j in range(self.image_res):
                if self.canvas.itemcget(self.pixel_rects[i][j], "fill") != self.color_bg:
                    self.canvas.itemconfig(self.pixel_rects[i][j], fill=self.color_bg)

    # gets if display is running
    def is_running(self):
        return self.running

    # close the display
    def close(self):
        self.running = False
        self.root.destroy()


#### __ define a drawing display object __ ####
class DrawingDisplay(Display):

    # constructor
    def __init__(self, image_res, pixel_size, title):

        # call to parent constructor
        Display.__init__(self, image_res, pixel_size, title)

        # set drawing variables
        self.draw_rad = 15

        self.mouse_pos = (None, None)
        self.mouse_pos_old = (None, None)

        # button pushed down
        self.root.bind('<ButtonPress-1>', lambda event: self.button_pressed(event))

        # mouse has moved
        self.root.bind('<B1-Motion>', lambda event: self.movement(event))

        # button released
        self.root.bind('<ButtonRelease-1>', lambda event: self.button_released())

        # reset the canvas when R pressed
        self.root.bind('r', lambda event: self.reset())

    # reset the canvas
    def reset(self):
        for i in range(self.image_res):
            for j in range(self.image_res):
                if self.canvas.itemcget(self.pixel_rects[i][j], "fill") != self.color_bg:
                    self.canvas.itemconfig(self.pixel_rects[i][j], fill=self.color_bg)
                    self.pixels[i, j] = 0

    # get the contained pixel array
    def get_pixels(self):
        return self.pixels

    # button pressed down
    def button_pressed(self, event):
        self.mouse_pos = (event.x, event.y)
        self.paint_point(event.x, event.y)

    # reset the stroke
    def button_released(self):
        self.mouse_pos_old = (None, None)
        self.mouse_pos = (None, None)

    # mouse moved
    def movement(self, event):
        self.mouse_pos_old = self.mouse_pos
        self.mouse_pos = (event.x, event.y)
        self.paint_line()

    # paint a line between last two mouse positions
    def paint_line(self):
        (x0, y0, x1, y1) = (self.mouse_pos_old[0], self.mouse_pos_old[1], self.mouse_pos[0], self.mouse_pos[1])
        if x0 == None or y0 == None or x1 == None or y1 == None: return

        self.paint_point(x0, y0)
        self.paint_point(x1, y1)

        (x0, y0) = (x0 - x0 % self.pixel_size + self.pixel_size//2, y0 - y0 % self.pixel_size + self.pixel_size//2)
        (x1, y1) = (x1 - x1 % self.pixel_size + self.pixel_size//2, y1 - y1 % self.pixel_size + self.pixel_size//2)

        (x0, y0) = (x0//self.pixel_size, y0//self.pixel_size)
        (x1, y1) = (x1//self.pixel_size, y1//self.pixel_size)

        if abs(x0-x1) >= abs(y0-y1):
            dist = abs(x0-x1)
            x_major = True
        else:
            dist = abs(y0-y1)
            x_major = False
        if dist < 1: return

        gap = self.draw_rad
        rad = self.draw_rad//self.pixel_size

        if x_major and y0-y1 != 0:
            gap += abs((y0-y1)/dist) * 0.414 * self.draw_rad
        if x_major == False and x0-x1 != 0:
            gap += abs((x0-x1)/dist) * 0.414 * self.draw_rad

        gap = int(gap//self.pixel_size)
        dist2 = min( (rad, dist) )

        self.paint_point(int((x0 + (x1-x0) * dist2/dist) * self.pixel_size), int((y0 + (y1-y0) * dist2/dist) * self.pixel_size))
        self.paint_point(int((x0 + (x1-x0) * (dist - dist2)/dist) * self.pixel_size), int((y0 + (y1-y0) * (dist - dist2)/dist) * self.pixel_size))

        for i in range(rad, dist + 1 - rad):
            x = int(x0 + (x1-x0) * i/dist)
            y = int(y0 + (y1-y0) * i/dist)

            if x_major and x >= 0 and x < self.image_res:
                for j in range(-gap, gap + 1):
                    if y + j >= 0 and y + j < self.image_res:
                        self.paint_rect(x, y + j)
            elif x_major == False and y >= 0 and y < self.image_res:
                for j in range(-gap, gap + 1):
                    if x + j >= 0 and x + j < self.image_res:
                        self.paint_rect(x + j, y)

    # paint a point onto the canvas
    def paint_point(self, pos_x, pos_y):
        if pos_x == None or pos_y == None: return
        gap = self.draw_rad//self.pixel_size

        (x, y) = (pos_x//self.pixel_size, pos_y//self.pixel_size)
        for i in range(-gap, gap + 1):
            for j in range(-gap, gap + 1):
                if i*i + j*j <= (gap + 0.5)*(gap + 0.5) and i + x >= 0 and i + x < self.image_res and j + y >= 0 and j + y < self.image_res:
                    self.paint_rect(x + i, y + j)

    # set the color of the rect indexed at [x, y]
    def paint_rect(self, x, y):
        self.canvas.itemconfig(self.pixel_rects[x][y], fill=self.color_fg)
        self.pixels[x, y] = 1;

#### __ define a drawing display object __ ####
class ViewingDisplay(Display):

    # constructor
    def __init__(self, image_res, pixel_size, title):

        # call to parent constructor
        Display.__init__(self, image_res, pixel_size, title)

    # set the display to a pixel array
    def set_display(self, pixels):
        if self.image_res != pixels.image_res:
            print("ERROR: << incompatible image resolutions >>")
            return

        for i in range(self.image_res):
            for j in range(self.image_res):
                if self.pixels[i, j] != pixels[i, j]:
                    self.pixels[i, j] = pixels[i, j]
                    pixel = 255 - int(pixels[i, j] * 255)
                    color ='#%02x%02x%02x' % (pixel, pixel, pixel)
                    if self.canvas.itemcget(self.pixel_rects[i][j], "fill") != color:
                        self.canvas.itemconfig(self.pixel_rects[i][j], fill=color)
