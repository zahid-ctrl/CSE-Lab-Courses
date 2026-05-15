import pygame
import random
import math
import time

# --- Initialize ---
pygame.init()
WIDTH, HEIGHT = 900, 650
screen = pygame.display.set_mode((WIDTH, HEIGHT))
pygame.display.set_caption("AI Multi-Agent Chase Game")
clock = pygame.time.Clock()
font = pygame.font.SysFont(None, 36)

# --- Colors ---
WHITE = (255, 255, 255)
BLUE = (50, 100, 255)
RED = (255, 60, 60)
BLACK = (0, 0, 0)
GREEN = (50, 180, 50)

# --- Player ---
player_size = 30
player_x = WIDTH // 2
player_y = HEIGHT // 2
player_speed = 5

# --- Enemy settings ---
enemy_size = 22
enemy_speed = 2.2
enemies = []  # List of enemies
max_enemies = 20  # Max enemies allowed

# --- Obstacles ---
obstacles = []
for _ in range(12):
    x = random.randint(50, WIDTH - 120)
    y = random.randint(50, HEIGHT - 120)
    obstacles.append(pygame.Rect(x, y, 70, 70))

# --- Countdown ---
for i in range(3, 0, -1):
    screen.fill(WHITE)
    text = font.render(f"Game Starts in {i}", True, BLACK)
    screen.blit(text, (WIDTH // 2 - 120, HEIGHT // 2))
    pygame.display.update()
    time.sleep(1)

# --- Start ---
start_time = time.time()
running = True
score = 0
enemy_spawn_score = 2  # Spawn new enemy every X score points

def spawn_enemy():
    """Spawn one enemy far from player and existing enemies."""
    while True:
        x = random.randint(0, WIDTH - enemy_size)
        y = random.randint(0, HEIGHT - enemy_size)
        distance = math.sqrt((x - player_x)**2 + (y - player_y)**2)

        # Ensure enemy doesn't spawn too close to player or others
        too_close = any(math.sqrt((x - e[0])**2 + (y - e[1])**2) < 100 for e in enemies)

        if distance > 200 and not too_close:
            enemies.append([x, y])
            break

# Start with one enemy
spawn_enemy()

# --- Main Game Loop ---
while running:
    screen.fill(WHITE)

    # --- Events ---
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            running = False

    # --- Player Movement ---
    keys = pygame.key.get_pressed()
    if keys[pygame.K_LEFT]: player_x -= player_speed
    if keys[pygame.K_RIGHT]: player_x += player_speed
    if keys[pygame.K_UP]: player_y -= player_speed
    if keys[pygame.K_DOWN]: player_y += player_speed

    # Keep player in screen
    player_x = max(0, min(WIDTH - player_size, player_x))
    player_y = max(0, min(HEIGHT - player_size, player_y))

    player_rect = pygame.Rect(player_x, player_y, player_size, player_size)

    # --- Draw obstacles ---
    for obs in obstacles:
        pygame.draw.rect(screen, GREEN, obs)

    # --- Enemy Movement ---
    for enemy in enemies:
        dx = player_x - enemy[0]
        dy = player_y - enemy[1]
        distance = math.sqrt(dx**2 + dy**2)

        if distance != 0:
            enemy[0] += (dx / distance) * enemy_speed
            enemy[1] += (dy / distance) * enemy_speed

        enemy_rect = pygame.Rect(enemy[0], enemy[1], enemy_size, enemy_size)
        pygame.draw.rect(screen, RED, enemy_rect)

        # Collision with player
        if player_rect.colliderect(enemy_rect):
            running = False

    # --- Increase score ---
    score = int(time.time() - start_time)

    # --- Spawn new enemies over time ---
    if score != 0 and score % enemy_spawn_score == 0:
        if len(enemies) < max_enemies:
            spawn_enemy()
            enemy_spawn_score += 2  # next enemy spawns after more score points

    # --- Draw player ---
    pygame.draw.rect(screen, BLUE, player_rect)

    # --- Draw score ---
    score_text = font.render(f"Score: {score}", True, BLACK)
    screen.blit(score_text, (10, 10))

    pygame.display.update()
    clock.tick(60)

# --- Game Over Screen ---
screen.fill(WHITE)
game_over = font.render(f"Game Over! Score: {score}", True, RED)
screen.blit(game_over, (WIDTH // 2 - 150, HEIGHT // 2))
pygame.display.update()
time.sleep(3)

pygame.quit()