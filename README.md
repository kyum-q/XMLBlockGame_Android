# 안드로이드 블록깨기 게임
XML을 통해 만들어지는 Block Shooting Game 게임 (Android 버전)
<br>
 
## ✍🏻&nbsp; 작품 소개
**아이템을 맞춰 점수를 획득하세요**<br>
<br>
배경 소리와 함께 즐거운 블록 맞추기 게임 BlockGame을 즐겨보세요<br>
블록은 움직이기도 안움직이기도 해요<br>
블록의 점수는 모두 다릅니다<br>
<br> 
목표점수에 도달하면 게임 Clear!
<br>
<br>
블록은 다양한 테마로 변경할 수 있어요
<br>

## 🖥&nbsp; 실행 화면
<br>

![Group 15](https://user-images.githubusercontent.com/109158497/236491542-11320c12-cfb6-47f9-84ef-4db213f36784.png)
**<p align="center">[게임 스크린 샷]</p>**
<br>


![화면 실행 영상](https://github.com/kyum-q/AndroidBlockGame/assets/109158497/72cf84e9-3c4d-4cd9-8914-7343ae0b6b09)
**<p align="center">[게임 실행 영상]</p>**
<br>

## 🧷&nbsp; 클래스 및 인터페이스 설명

#### Interface Summary
GoneBlockInterface: 맞으면 사라지는 블록일 경우에 필요한 함수를 가진 interface

#### Class Summary
AttackThread: attack의 움직임을 나타내는 Thread<br>
BlockGameFrame: Block 게임의 Frame을 설정하는 class<br>
DontGoneBlock: 게임의 움직이지 않고 사라지지 않은 블록 이미지 레이블(extends JLabel)<br>
GameInitPanel: Block 게임의 타이틀 혹은 게임 종료 후 Panel<br>
GamePanel: Block 게임 실행 Panel<br>
GameThread: game을 움직이는 Thread<br>
GoneBlock: 게임의 움직이지 않지만 사라지는 블록 이미지 레이블 (extends DontGoneBlock implements GoneBlockInterface)<br>
Music: 음악을 재생하는 class<br>
SideMoveAndGoneBlock: 게임의 좌우로 움직이고 사라지는 블록 이미지 레이블(extends SideMoveBlock implements GoneBlockInterface)<br>
SideMoveBlock: 게임의 좌우로 움직이고 사라지지 않는 블록 이미지 레이블(extends DontGoneBlock)<br>

## 📍&nbsp; 시스템 계층 구조

![그림2](https://github.com/kyum-q/AndroidBlockGame/assets/109158497/98dade82-eeac-4dee-905b-b9b6e1bd8ed5)
**<p align="center">[BlockGame Block 계층구조]</p>**
<br>

![그림3](https://github.com/kyum-q/AndroidBlockGame/assets/109158497/71567211-0664-4d6d-904c-0e4a7f6eb6ac)
**<p align="center">[Block 안에 GoneBlockInterface 계층 구조]</p>**
<br>

## 🔍&nbsp; 개발 언어
<img src="https://img.shields.io/badge/JAVA-FF7800?style=for-the-badge&logo=Java&logoColor=#7F52FF"> <img src="https://img.shields.io/badge/android-3DDC84?style=for-the-badge&logo=Android&logoColor=white">
