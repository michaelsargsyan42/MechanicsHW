import matplotlib.pyplot as plt
import matplotlib.patches as patches

def compute_accelerations(M1, M2, M3, nyu1, nyu2, nyu3, g):
    a1 = (M3 * g) / M1
    a2 = (M3 * g) / M2
    a3 = g
    return a1, a2, a3

def compute_max_distance(L):
    return L

def visualize_motion(M1, M2, M3, nyu1, nyu2, nyu3, L, dt):
    a1, a2, a3 = compute_accelerations(M1, M2, M3, nyu1, nyu2, nyu3, g)
    D = compute_max_distance(L)

    x1 = 0
    x2 = L
    x3 = 0
    time_elapsed = 0

    fig, ax = plt.subplots()
    ax.set_xlim(-L, 2*L)
    ax.set_ylim(-L, L)
    M1_patch = patches.Rectangle((x1-L/2, -0.1), L, 0.2, fc='blue')
    M2_patch = patches.Circle((x2, 0), 0.1, fc='red')
    M3_patch = patches.Rectangle((x3-L/2, -L), 0.2, L, fc='green')

    ax.add_patch(M1_patch)
    ax.add_patch(M2_patch)
    ax.add_patch(M3_patch)

    def update_positions(dt, a1, a2, a3, nyu1, nyu2, nyu3):
        nonlocal x1, x2, x3, time_elapsed

        time_elapsed += dt

        nyu1 += a1 * dt
        nyu2 += a2 * dt
        nyu3 -= a3 * dt


        x1 += nyu1 * dt
        x2 += nyu2 * dt
        x3 -= nyu3 * dt  

        M1_patch.set_xy((x1-L/2, -0.1))
        M2_patch.center = (x2, 0)
        M3_patch.set_xy((x3-L/2, -L))

 
        M2_patch.set_fill(True if nyu2 > 0 else False)

        plt.pause(dt)

    while x2 > 0:
        update_positions(dt, a1, a2, a3, nyu1, nyu2, nyu3)

    plt.show()


M1 = float(input("Enter mass for M1: "))
M2 = float(input("Enter mass for M2: "))
M3 = float(input("Enter mass for M3: "))
nyu1 = float(input("Enter initial velocity for M1 (nyu1): "))
nyu2 = float(input("Enter initial velocity for M2 (nyu2): "))
nyu3 = float(input("Enter initial velocity for M3 (nyu3): "))
g = 9.81
L = 2
dt = 0.01
visualize_motion(M1, M2, M3, nyu1, nyu2, nyu3, L, dt)
