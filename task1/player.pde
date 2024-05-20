void loadPlayer()
{
  playerFrames = loadImage("Stick.png");
  plrBlocks = new PImage[(playerFrames.width/playerW)*(playerFrames.height/playerH)];
  for(int i = 0; i < playerFrames.height/playerH; ++i)
  {
    for(int j = 0; j < playerFrames.width/playerW; ++j)
    {
      plrBlocks[i*playerFrames.width/playerW+j] = playerFrames.get(j*playerW, i*playerH, playerW, playerH);
    }
  }
}

void displayPlayer()
{
  int firstJumpingAnimationLength = 7;
  if(goingUp)
  {
    if(frameCount-startedJumping <= firstJumpingAnimationLength)
      image(plrBlocks[0], playerX, playerY);
    else
      image(plrBlocks[1], playerX, playerY);
  }
  else if(goingDown)
  {
    image(plrBlocks[1], playerX, playerY);
  }
  else if(goingRight)
  {
    image(plrBlocks[frameCount%14/2+2], playerX, playerY);
  }
  else if(goingLeft)
  {
    image(plrBlocks[frameCount%14/2+11], playerX, playerY);
  }
  else
  {
    image(plrBlocks[10], playerX, playerY);
  }
}

void controlPlayer()
{
  if(keyPressed)
  {
    switch(keyCode)
    {
      case UP:
        if(playerY > 0 && !isOkayToFall() && goingUp == false && !isSolid(playerX/blockX, (playerY-2)/blockY) && !isSolid((playerX+playerW)/blockX, (playerY-2)/blockY))
        {
          startedJumping = frameCount;
          startedFalling = frameCount;
          goingUp = true;
          fallingSpeed = -2;
        }
        break;
      case LEFT:
        if(isOkayToGoLeft())
        {
          goingLeft = true;
          goingRight = false;
          playerX-=2;
        }
        break;
      case RIGHT:
        if(isOkayToGoRight())
        {
          goingRight = true;
          goingLeft = false;
          playerX+=2;
        }
        break;
    }
  }
  else
  {
    goingRight = false;
    goingLeft = false;
    goingUp = false;
  }
}

void gravity()
{
  if(fallingSpeed != 0)
    playerY += fallingSpeed;
  if(!isOkayToFall())
  {
    fallingSpeed = 0;
    goingDown = false;
  }
  else if(fallingSpeed == 0)
  {
    startedFalling = frameCount;
    goingUp = false;
    goingDown = true;
    fallingSpeed = 1;
  }
  else if((frameCount-startedFalling)%30 == 0)
  {
    fallingSpeed++;
  }
}

boolean isOkayToFall()
{
  if(playerY >= worldHeight*blockY)
  {
    return false;
  }
  else if(playerY+playerH+fallingSpeed >= worldHeight*blockY)
  {
    return true;
  }
  if(isSolid(playerX/blockX, (playerY+playerH+fallingSpeed)/blockY) || 
     isSolid(playerX/blockX, (playerY+fallingSpeed)/blockY) ||
     isSolid((playerX+playerW-1)/blockX, (playerY+fallingSpeed)/blockY) || 
     isSolid((playerX+playerW-1)/blockX, (playerY+playerH+fallingSpeed-1)/blockY) || 
     playerY+fallingSpeed < 0)
    return false;
  return true;
}

boolean isOkayToGoRight()
{
  if(!(playerX < worldWidth*blockX) || playerY+playerH >= worldHeight*blockY)
  {
    return false;
  }
  if(isSolid((playerX+playerW+2)/blockX, (playerY+playerH-1)/blockY) || isSolid((playerX+playerW+2)/blockX, playerY/blockY))
    return false;
  return true;
}

boolean isOkayToGoLeft()
{
  if(!(playerX > 0) || playerY+playerH >= worldHeight*blockY)
  {
    return false;
  }
  if(isSolid((playerX-2)/blockX, (playerY+playerH-1)/blockY) || isSolid((playerX-2)/blockX, playerY/blockY))
    return false;
  return true;
}

void deathScreen()
{
  if(playerY >= worldHeight*blockY && !dead)
  {
    dead = true;
    diedOnFrame = frameCount;
  }
  else if(dead && (frameCount-diedOnFrame)/300 != 1)
  {
    textSize(height/10);
    text("YOU DIED!", width/2-translX, height/2-translY);
  }
  else if(dead)
  {
    dead = false;
    whichWorld = -1;
  }
}

boolean isOnBlock(int x, int y)
{
  int pX, pXW;
  int pY, pYH;
  pX = playerX/blockX;
  pXW = (playerX+playerW-1)/blockX;
  pY= playerY/blockY;
  pYH = (playerY+playerH-1)/blockY;
  if(x == pX)
  {
    if(y == pY)
    {
      return true;
    }
    else if(y == pYH)
    {
      return true;
    }
  }
  else if(x == pXW)
  {
    if(y == pY)
    {
      return true;
    }
    else if(y == pYH)
    {
      return true;
    }
  }
  return false;
}

boolean checkForWin()
{
  if(isOnBlock(doorCoords[whichWorld][0], doorCoords[whichWorld][1]))
  {
    return true;
  }
  return false;
}
