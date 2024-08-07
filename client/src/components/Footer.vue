<template>
  <div class="footer">
    <div class="container">
      <div class="ft-outer">
        <router-link to="/" class="ft-logo">
          <strong>SHOP</strong>
        </router-link>
      </div>
      <div class="ft-outer">
        <h4 class="ft-title">팀원 소개</h4>
        <ul>
          <li>1902110603 김진섭 - 프론트엔드</li>
          <li>1902110606 김현준 - 퍼블리싱</li>
          <li>1902110595 구남준 - 데이터베이스, 팀장</li>
          <li>1902110621 오성찬 - 백엔드</li>
        </ul>
      </div>
      <div class="ft-outer">
        <h4 class="ft-title">프로젝트 소개</h4>
        <p>본 팀 프로젝트는 웹 쇼핑몰을 주제로 선정하였습니다.<br>
          코로나19로 인해 외부활동이 제한됨에 따라, 평소 프로그래밍에 관심이 있던 학우들끼리 팀을 구성하게 되었습니다.<br>
          프론트엔드 프레임워크는 Vue.js, <br>백엔드 프레임워크는 Spring Boot, <br>데이터베이스는 MySql을 사용하여 제작하였습니다.
        </p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Footer'
}
</script>

<style>
.footer {
  position: relative;
  bottom: 0;
  background: #000;
  height: 240px;
}
.footer .container {
  padding: 24px 0;
  display: flex;
  flex-direction: row;
  height: 100%;
}
.footer .ft-outer {
  color: #fff;
  width: 33.33%;
  margin-left: 48px;
}
.footer .ft-outer:nth-child(1) {
  display: flex;
  margin-left: 0;
}
.footer .ft-outer:nth-child(1) .ft-logo {
  margin-top: 24px;
}
.footer .ft-outer .ft-logo strong {
  font-size: 40px;
  color: #fff;
}
.footer .ft-outer .ft-title {
  margin-bottom: 16px;
}
.footer .ft-outer ul {
  padding-left: 0;
}
.footer .ft-outer ul li {
  margin-bottom: 10px;
}
.footer .ft-outer p {
  display: inline;
  word-break: break-all;
  word-wrap: break-word;
  vertical-align: top;
  letter-spacing: -0.5px;
}

</style>
