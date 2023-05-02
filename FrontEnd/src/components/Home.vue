<template>
  <div class="layout">
    <Layout :style="{minHeight: '100vh'}">
        <Header class="title" style="z-index: 999">
          <Row>
            <Col span="8" style="text-align: left; line-height: 54px">Water Quality Management and Forecasting System</Col>
            <Col span="4" offset="12" style="line-height: 54px">
              <Dropdown @on-click="dropDownClicked">
                <span href="javascript:void(0)">
                  {{currentUser.username}}
                  <Icon type="ios-arrow-down"></Icon>
                </span>
                <DropdownMenu slot="list">
                  <DropdownItem name="info">User Info</DropdownItem>
                  <DropdownItem name="editPassword">Update Password</DropdownItem>
                  <DropdownItem name="logout">Log Out</DropdownItem>
                </DropdownMenu>
              </Dropdown>
            </Col>
          </Row>
        </Header>
      <Layout>
        <Sider class="sider">
          <Menu class="menu" :open-names="['1','2','3']" theme="light" width="auto" :active-name="activeName">
            <Submenu name="1">
              <template slot="title">
                <Icon type="ios-archive" />
                Management
              </template>
              <MenuItem name="/recent" to="/recent">Recent Data</MenuItem>
              <MenuItem name="/manage" to="/manage">History Data</MenuItem>
              <MenuItem name="/trend" to="trend">Data Trend</MenuItem>
            </Submenu>

            <Submenu name="2">
              <template slot="title">
                <Icon type="md-trending-up" />
                Forecasting
              </template>
              <MenuItem name="/predict" to="predict">Make Prediction</MenuItem>
              <MenuItem name="/train" to="train">Train Models</MenuItem>
            </Submenu>

            <Submenu name="3" style="display: none" id="user_menu">
              <template slot="title">
                <Icon type="ios-contact" />
                Administration
              </template>
              <MenuItem name="/user" to="user">User Management</MenuItem>
            </Submenu>
          </Menu>
        </Sider>
        <Layout>
          <Content>
            <router-view></router-view>
          </Content>
          <Footer class="footer">
            &copy; Yifan Pan &nbsp &nbspNanjing University of Posts and Telecommunications
          </Footer>
        </Layout>
      </Layout>
    </Layout>
    <Modal v-model="info_modal_flag" title="User Info" cancel-text="">
      <Row class="editLine">
        <Col span="4" offset="3" style="text-align: left">
          <span>Username</span>
        </Col>
        <Col span="6" offset="1">
          <span>{{currentUser.username}}</span>
        </Col>
      </Row>
      <Row class="editLine">
        <Col span="4" offset="3" style="text-align: left">
          <span>Role</span>
        </Col>
        <Col span="6" offset="1">
          <span>{{currentUser.role}}</span>
        </Col>
      </Row>
      <Row class="editLine">
        <Col span="4" offset="3" style="text-align: left">
          <span>Authority</span>
        </Col>
        <Col span="12" offset="1">
          <span>{{currentUser.authority}}</span>
        </Col>
      </Row>
    </Modal>
    <Modal v-model="pwd_modal_flag" title="Update Password" cancel-text="Cancel" ok-text="Submit" @on-ok="editPassword" >
      <Row class="editLine">
        <Col span="6" offset="3">
          <span>Password</span>
        </Col>
        <Col span="8" offset="1">
          <Input size="small" type="password" v-model="password.originPassword"></Input>
        </Col>
      </Row>
      <Row class="editLine">
        <Col span="6" offset="3">
          <span>New Password</span>
        </Col>
        <Col span="8" offset="1">
          <Input size="small" type="password" v-model="password.newPassword"></Input>
        </Col>
      </Row>
      <Row class="editLine">
        <Col span="6" offset="3">
          <span>Confirm Password</span>
        </Col>
        <Col span="8" offset="1">
          <Input size="small" type="password" v-model="password.confirmPassword"></Input>
        </Col>
      </Row>
    </Modal>
  </div>
</template>

<script>
    export default {
        name: "Home",
        data(){
          return {
            activeName: this.$route.path,
            currentUser: {},
            password: {
              originPassword: "",
              newPassword: "",
              confirmPassword: ""
            },
            info_modal_flag: false,
            pwd_modal_flag: false,
            admin_flag: false
          }
        },
        watch:{
          '$route'(){
            this.activeName = this.$route.path
          }
        },
        created() {
          this.getCurrentUser();
        },
        methods: {
          getCurrentUser() {
            let _this = this;
            this.axios.get("/user/current")
              .then(function(response) {
                if(response.data.status === "noLogin") {
                  _this.$router.replace({path: '/'});
                } else {
                  _this.currentUser = response.data;
                  if(_this.currentUser.role.id === 1) {
                    _this.currentUser.role = "Super Admin";
                    _this.currentUser.authority = "All Authorities"
                    let user_menu = document.getElementById("user_menu");
                    user_menu.style.display = "block";
                  } else if(_this.currentUser.role.id === 2) {
                    _this.currentUser.role = "Admin";
                    _this.currentUser.authority = "query, create, modify, delete water quality data; make predictions; train models"
                  } else {
                    _this.currentUser.role = "User";
                    _this.currentUser.authority = "query water quality data; make predictions"
                  }
                }
            })
          },

          dropDownClicked(name) {
            switch(name) {
              case "logout": this.logout(); break;
              case "info": this.info_modal_flag = true; break;
              case "editPassword": this.pwd_modal_flag = true; break;
            }
          },

          logout() {
            let _this = this;
            this.axios.get("/logout")
              .then(function (response) {
                if(response.data.status === "success") {
                  _this.$router.replace({path: '/'});
                } else {
                  _this.$Message.error("Log out failed!")
                }
              })
          },

          editPassword() {
            let _this = this;
            if(this.checkPassword()) {
              this.axios.post("/user/editPassword/" + this.currentUser.id.toString(), this.qs.stringify({
                originPassword: _this.password.originPassword,
                newPassword: _this.password.newPassword
              }))
                .then(function(response) {
                  if(response.data.status === "success") {
                    _this.$Message.success("Update password successfully!");
                  } else if(response.data.status === "error") {
                    _this.$Message.error("Wrong password!");
                  } else {
                    _this.$Message.error("Update password failed!");
                  }
                })
            }
            this.password.originPassword = '';
            this.password.newPassword = '';
            this.password.confirmPassword = '';
          },

          checkPassword() {
            if(this.password.originPassword === '' || this.password.newPassword === ''
              || this.password.confirmPassword === '') {
              this.$Message.error("Input is empty!");
              return false;
            } else if(this.password.newPassword !== this.password.confirmPassword) {
              this.$Message.error("New password is inconsistent!");
              return false;
            }
            return true;
          }
        }
    }
</script>

<style scoped>
  .title {
    font-size: 20px;
    color: #ffffff;
    background-color: #336699;
    opacity: 0.9;
    height: 54px;
  }

  .sider {
    background-color: #E8E8E8;
  }

  .menu {
    background-color: #E8E8E8;
  }

  .editLine {
    font-size: 12px;
    line-height: 24px;
    margin-top: 10px;
    margin-bottom: 10px;
  }


  .footer {
    font-size: 16px;
    margin-top: 100px;
    text-align: center;
    color: gray;
  }

</style>
