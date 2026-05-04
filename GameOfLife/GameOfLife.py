import torch
import torch.nn.functional as F
import tkinter as tk

ROWS = 150
COLS = 200
CELL_SIZE = 6
DELAY = 5

device = "cuda" if torch.cuda.is_available() else "cpu"

grid = torch.randint(0, 2, (1, 1, ROWS, COLS), dtype=torch.float32, device=device)

kernel = torch.tensor(
    [[[[1,1,1],
       [1,0,1],
       [1,1,1]]]],
    dtype=torch.float32,
    device=device
)

def update_grid():
    global grid
    neighbors = F.conv2d(grid, kernel, padding=1)

    birth = (grid == 0) & (neighbors == 3)
    survive = (grid == 1) & ((neighbors == 2) | (neighbors == 3))

    grid = (birth | survive).float()

root = tk.Tk()
root.title("Game of Life (PyTorch)")

canvas = tk.Canvas(
    root,
    width=COLS * CELL_SIZE,
    height=ROWS * CELL_SIZE,
    bg="black"
)
canvas.pack()

def draw():
    canvas.delete("all")
    g = grid[0, 0].detach().cpu()

    for r in range(ROWS):
        for c in range(COLS):
            if g[r][c] == 1:
                x1 = c * CELL_SIZE
                y1 = r * CELL_SIZE
                x2 = x1 + CELL_SIZE
                y2 = y1 + CELL_SIZE
                canvas.create_rectangle(x1, y1, x2, y2, fill="white", outline="")

def loop():
    update_grid()
    draw()
    root.after(DELAY, loop)

loop()
root.mainloop()