<template>
  <div class="content">
    <div class="title">
      Water Quality Management and Forecasting System
    </div>
    <div id="login">
      <div class="login_title">Login</div>
      <Form ref="user" :model="user" :rules="ruleInline">
        <FormItem prop="username">
          <Input type="text" v-model="user.username" placeholder="Username">
            <Icon type="ios-person-outline" slot="prepend"></Icon>
          </Input>
        </FormItem>
        <FormItem prop="password" id="password">
          <Input type="password" v-model="user.password" placeholder="Password">
            <Icon type="ios-lock-outline" slot="prepend"></Icon>
          </Input>
        </FormItem>
        <Row class="register">
            <a @click="modal_flag = true">No Account？Click to Register</a>
        </Row>
        <FormItem id="submitButton">
          <Button type="primary" size="large" @click="handleSubmit('user')">Login</Button>
        </FormItem>
      </Form>
    </div>
    <div class="footer">
      <span> &copy;Yifan Pan &nbsp &nbspNanjing University of Posts and Telecommunications</span>
    </div>
    <Modal v-model="modal_flag" title="Registration" cancel-text="Cancel" ok-text="Register" @on-ok="register" >
      <Row class="editLine">
        <Col span="6" offset="3">
          <span>Username</span>
        </Col>
        <Col span="8" offset="1">
          <Input v-model="newUser.username"></Input>
        </Col>
      </Row>
      <Row class="editLine">
        <Col span="6" offset="3">
          <span>Password</span>
        </Col>
        <Col span="8" offset="1">
          <Input type="password" v-model="newUser.password"></Input>
        </Col>
      </Row>
      <Row class="editLine">
        <Col span="6" offset="3">
          <span>Confirm Password</span>
        </Col>
        <Col span="8" offset="1">
          <Input type="password" v-model="newUser.confirmPassword"></Input>
        </Col>
      </Row>
    </Modal>
  </div>
</template>

<script>
    export default {
        name: "Login",
        data() {
          return {
            user: {
              username: '',
              password: ''
            },
            ruleInline: {
              username: [
                { required: true, message: 'Please input username', trigger: 'blur' }
              ],
              password: [
                { required: true, message: 'Please input password', trigger: 'blur' },
              ]
            },
            modal_flag: false,
            newUser: {
              username: "",
              password: "",
              confirmPassword: ""
            }
          }
        },
        methods: {
          handleSubmit(user) {
            let _this = this;
            this.$refs[user].validate((valid) => {
              if (valid) {
                _this.axios({
                  url: "/login",
                  method: 'POST',
                  data: {
                    username: _this.user.username,
                    password: _this.user.password
                  },
                  transformRequest: [function (data) {
                    // Do whatever you want to transform the data
                    let ret = ''
                    for (let it in data) {
                      ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
                    }
                    return ret
                  }],
                  headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                  }
                })
                  .then(function(response) {
                    console.log(response.data)
                    if(response.data.status === "success") {
                      _this.$router.replace({path: '/manage'});
                    } else {
                      _this.$Message.error('Wrong username or password!');
                    }
                  })
              } else {
                this.$Message.error('Wrong username or password!');
              }
            })
          },

          check() {
            if(this.newUser.username === '' || this.newUser.password === '' || this.newUser.confirmPassword === ''){
              this.$Message.error("Input is empty!");
              return false;
            }
            if(this.newUser.password !== this.newUser.confirmPassword) {
              this.$Message.error("New password is inconsistent!");
              return false;
            }
            return true;
          },

          register() {
            if(this.check()) {
              let _this = this;
              this.axios.post("/user/register", this.qs.stringify({
                username: _this.newUser.username,
                password: _this.newUser.password
              }))
                .then(function(response) {
                  if(response.data.status === "success") {
                    _this.$Message.success("Register succeed!");
                  } else if(response.data.status === "duplicate") {
                    _this.$Message.error("Username already exists!");
                  } else {
                    _this.$Message.error("Register failed!");
                  }
                })
            }
            this.newUser.username = "";
            this.newUser.password = "";
            this.newUser.confirmPassword = "";
          }
        }
    }
</script>

<style scoped>
  #login{
    font-size: 14px;
    margin: 0 auto;
    margin-top: 80px;
    padding: 50px;
    width: 360px;
    height: 280px;
    box-shadow: darkgrey 2px 2px 10px 2px;
    background: #ffffff;
  }

  .title {
    margin-top: 80px;
    font-size: 46px;
    color: white;
  }

  .login_title {
    font-size: 20px;
    font-weight: bold;
    color: #3399FF;
    margin-bottom: 15px;
  }

  #login img{
    margin-bottom: 20px;
  }

  #submitButton{
    margin-top: 20px;
  }

  .footer {
    font-size: 16px;
    margin-top: 100px;
    text-align: center;
    color: gray;
  }

  .register{
    margin-top: 15px !important;
  }

  #password {
    margin-bottom: 10px;
  }

  #submitButton {
    margin-top: 10px;
  }

  .editLine {
    font-size: 12px;
    line-height: 32px;
    margin-top: 10px;
    margin-bottom: 10px;
  }

</style>
